package shoppingMall.gupang.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.exception.coupon.NoCouponException;
import shoppingMall.gupang.exception.order.NoDeliveryCouponException;
import shoppingMall.gupang.web.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.web.controller.order.dto.OrderItemDto;
import shoppingMall.gupang.web.controller.order.dto.OrderReturnDto;
import shoppingMall.gupang.domain.discount.DiscountPolicy;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.DeliveryCoupon;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.order.AlreadyCanceledOrderException;
import shoppingMall.gupang.exception.order.KeyAttemptFailException;
import shoppingMall.gupang.exception.order.NoOrderException;
import shoppingMall.gupang.redis.facade.RedissonLockStockFacade;
import shoppingMall.gupang.repository.coupon.CouponRepository;
import shoppingMall.gupang.repository.coupon.DeliveryCouponRepository;
import shoppingMall.gupang.repository.delivery.DeliveryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.order.OrderRepository;
import shoppingMall.gupang.repository.orderItem.OrderItemRepository;

import java.time.LocalDateTime;
import java.util.*;

import static shoppingMall.gupang.domain.OrderStatus.CANCEL;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final DiscountPolicy discountPolicy;
    private final DeliveryRepository deliveryRepository;
    private final CouponRepository couponRepository;
    private final DeliveryCouponRepository deliveryCouponRepository;
    private final RedissonLockStockFacade redissonLockStockFacade;

    @Override
    public Long order(OrderCouponDto dto, String memberEmail) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 회원이 없습니다.");
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        List<OrderItem> orderItems = new ArrayList<>();

        // 쿠폰이 존재하지 않으면 getCoupons나 getDeliverycoupons가 nullPoint exception 발생하기 때문에 초기화 필요
        List<Coupon> coupons = new ArrayList<>();
        Optional<DeliveryCoupon> deliveryCoupon = Optional.empty();
        if (dto.getCouponIds() != null) {
            coupons = checkAndGetCoupons(dto.getCouponIds(), member);
        }
        if (dto.getDeliveryCouponId() != null) {
            deliveryCoupon = checkAndGetDeliveryCoupon(dto.getDeliveryCouponId(), member);
        }

        getOrderItemsWithCoupons(dto.getOrderItemDtos(), coupons, orderItems);
        boolean isDeliveryCouponExist = deliveryCoupon.isPresent();
        Delivery delivery = getDelivery(isDeliveryCouponExist, dto.getAddress());

        // deliveryCoupon 존재한다면 사용한다.
        if (isDeliveryCouponExist) {
            DeliveryCoupon newDeliveryCoupon = deliveryCoupon.orElse(null);
            newDeliveryCoupon.useCoupon();
        }

        // 전체 값을 구한 뒤, 배송비와 멤버십 할인 여부 및 얼마나 할인할지를 정한다.
        int totalPrice = getTotalPrice(delivery.getDeliveryFee(), orderItems, isMemberShip);

        Order order = Order.createOrder(LocalDateTime.now(), member, delivery, isMemberShip, OrderStatus.ORDER,
                orderItems, totalPrice);

        orderRepository.save(order);
        return order.getId();
    }

    private List<Coupon> checkAndGetCoupons(List<Long> couponIds, Member member) {
        // session의 멤버id와 coupon이 갖고 있는 member id가 맞으면 coupons에 추가
        List<Coupon> coupons = new ArrayList<>();
        for (Long couponId : couponIds) {
            Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
            Coupon coupon = optionalCoupon.orElse(null);
            if (coupon == null) {
                throw new NoCouponException("해당하는 쿠폰이 존재하지 않습니다.");
            }
            if (member.getId().equals(coupon.getMember().getId())) {
                coupon.checkCouponValid();
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    private Optional<DeliveryCoupon> checkAndGetDeliveryCoupon(Long deliveryCouponId, Member member){
        Optional<DeliveryCoupon> optionalDeliveryCoupon = deliveryCouponRepository.findById(deliveryCouponId);
        DeliveryCoupon deliveryCoupon = optionalDeliveryCoupon.orElse(null);
        if (deliveryCoupon == null) {
            throw new NoDeliveryCouponException("해당하는 배송 쿠폰이 존재하지 않습니다.");
        }

        if (deliveryCoupon.getMember().getId().equals(member.getId())) {
            deliveryCoupon.checkCouponValid();
            return Optional.of(deliveryCoupon);
        }
        return Optional.empty();
    }
    /*
        - 쿠폰이 여러개 들어오고 배송 쿠폰이 한개 들어오면 이 쿠폰들은 session을 이용해서 검증하고 맞는 것만 적용되도록 바꾸기
        - 쿠폰이 하나씩만 적용되는 시나리오로 변경하기 => deliveryCoupon은 List가 아닌 한개만 들어오도록 변경하기
        - 하지만 같은 상품이지만 나눠져서 들어올수도 있기 때문에(혹시나!) 그거에 대한 조치 필요
            - 사용된 itemid들을 모아 놓은 list를 통해 사용한 건 다시 사용하지 못하게 체크
        - 그래서 order 엔티티에 총 주문 비용 속성을 추가해 그곳에서 총 주문 비용을 관리해야 한다.
            - 그리고 order의 총 가격을 계산하고 배송비를 더할지 안할지 계산하기
        - 멤버십 할인도 order의 모든 상품 합에서 한번만 할인되어야 하는것이다.
        - 배송비 쿠폰 사용했으면 사용했다고 체크해야한다.
     */
    // 쿠폰이 하나씩만 적용되는 시나리오로 변경하기
    private void getOrderItemsWithCoupons(List<OrderItemDto> dtos, List<Coupon> coupons,
                                          List<OrderItem> orderItems) {
        /*
            - orderItem을 돌릴때 마다 인자로 가져온 coupons를 돌려서
            - 그 orderItem에 해당하는 상품에 맞고, 사용하지 않고, 기간이 지나지 않은
            - 쿠폰들을 적용한다.
         */
        /*
            - orderItem들 끼리 상품이 겹치지 않는 다는 것을 알 수 있다.
            - 보통 카트에 담긴 상품들을 보면 한 상품을 낱개로 담는게 아니라 숫자로 담기 때문이다.
            - 하지만 같은 상품이지만 나눠져서 들어올수도 있기 때문에(혹시나!) 그거에 대한 조치 필요
         */
        List<Long> alreadyCheckItemIds = new ArrayList<>();
        for (OrderItemDto dto : dtos) {
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item == null) {
                throw new NoItemException("해당 상품이 없습니다.");
            }
            int discountAmount = 0;

            for (Coupon coupon : coupons) {
                /*
                    - 쿠폰을 여러개 돌리면서 사용되지 않은 쿠폰이면서 쿠폰이 지원하는 상품과 현재 상품이 같을 때 쿠폰 할인 적용
                    - 아무리 사용되지 않은 쿠폰이라도 이미 사용된 상품에 다시 쿠폰이 적용될 수 없다.
                        - 물론 카트 로직상으로 카트안에서 orderItem끼리 상품id가 겹치지는 않겠지만 혹시 모르니 추가 하는 안전장치이다.
                 */
                if (coupon.getItem().getId().equals(item.getId())) {
                    if (!coupon.getUsed() && !alreadyCheckItemIds.contains(dto.getItemId())) {
                        discountAmount = coupon.getDiscountAmount();
                        coupon.useCoupon();
                        alreadyCheckItemIds.add(dto.getItemId());
                        break;
                    }
                }
            }
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getItemPrice(),
                    dto.getItemCount(), discountAmount);
            decreaseItemQuantity(dto.getItemId(), dto.getItemCount());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
    }

    private Delivery getDelivery(boolean isDeliveryCouponExist, Address address) {
        Delivery delivery = new Delivery(address, getDeliveryFee(isDeliveryCouponExist), DeliveryStatus.READY);
        deliveryRepository.save(delivery);
        return delivery;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderReturnDto> getOrderByMember(Long memberId, Pageable pageable) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 멤버가 없습니다.");
        }
        Page<Order> page = orderRepository.findByMember(member, pageable);
        return page.map(OrderReturnDto::new);
    }

    @Override
    public void cancelOrder(Long orderId) {
        List<Order> order = orderRepository.findOrderWithDelivery(orderId);
        if (order.size() == 0) {
            throw new NoOrderException("해당하는 주문이 없습니다.");
        }

        for (Order o : order) {
            if (o.getOrderStatus() == CANCEL) {
                throw new AlreadyCanceledOrderException("이미 취소된 주문입니다.");
            }
            o.cancel();
            for(OrderItem oi : o.getOrderItems()) {
                increaseItemQuantity(oi.getItem().getId(), oi.getItemCount());
            }
        }
    }

    private int getMembershipDiscountedPrice(IsMemberShip isMemberShip, int price) {
        return discountPolicy.discount(isMemberShip, price);
    }

    // valid한 deliveryCoupon이 하나라도 있다면 적용되면 0원이기 때문에 있으면 0원 return 아니면 3000원 return
    private int getDeliveryFee(boolean isDeliveryCouponExist) {
        if (isDeliveryCouponExist) {
            return 0;
        } else {
            return 3000;
        }
    }

    private void decreaseItemQuantity(Long itemId, int quantity) {
        try {
            redissonLockStockFacade.decrease(itemId, quantity);
        } catch (Exception e) {
            throw new KeyAttemptFailException("레디스 키 값을 가져오는 중에 문제가 발생하였습니다.");
        }
    }

    private void increaseItemQuantity(Long itemId, int quantity) {
        try {
            redissonLockStockFacade.increase(itemId, quantity);
        } catch (Exception e) {
            throw new KeyAttemptFailException("레디스 키 값을 가져오는 중에 문제가 발생하였습니다.");
        }
    }

    // 전체 가격을 구하고 나서 적용해야하는 배송비와 멤버십은 이곳에서 적용
    private int getTotalPrice(int deliveryFee, List<OrderItem> orderItems, IsMemberShip isMemberShip) {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        int membershipDiscountedPrice = getMembershipDiscountedPrice(isMemberShip, totalPrice);
        return totalPrice + deliveryFee - membershipDiscountedPrice;
    }
}
