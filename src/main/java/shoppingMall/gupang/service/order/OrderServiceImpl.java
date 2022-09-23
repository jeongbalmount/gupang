package shoppingMall.gupang.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.item.ItemFindDto;
import shoppingMall.gupang.controller.order.OrderDto;
import shoppingMall.gupang.discount.DiscountPolicy;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.exception.NoCouponException;
import shoppingMall.gupang.exception.NoItemException;
import shoppingMall.gupang.exception.NoMemberException;
import shoppingMall.gupang.exception.NoOrderException;
import shoppingMall.gupang.repository.coupon.CouponRepository;
import shoppingMall.gupang.repository.delivery.DeliveryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.order.OrderRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static shoppingMall.gupang.domain.IsMemberShip.MEMBERSHIP;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final DiscountPolicy discountPolicy;
    private final DeliveryRepository deliveryRepository;
    private final CouponRepository couponRepository;

    @Override
    public Long order(Long memberId, Address address, List<OrderDto> orderDtos) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 멤버가 없습니다.");
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(isMemberShip, address);

        List<OrderItem> orderItems = new ArrayList<>();

        getOrderItems(orderDtos, isMemberShip, orderItems);

        Order order = Order.createOrder(LocalDateTime.now(), member, delivery, isMemberShip,
                OrderStatus.ORDER, orderItems);

        return order.getId();
    }

    private void getOrderItems(List<OrderDto> orderDtos, IsMemberShip isMemberShip, List<OrderItem> orderItems) {
        for (OrderDto dto : orderDtos) {
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item != null) {
                OrderItem orderItem = OrderItem.createOrderItem(item,
                        getMembershipDiscountedPrice(isMemberShip, item.getItemPrice()), dto.getItemCount());
                orderItems.add(orderItem);
            }
        }
    }

    @Override
    public Order orderWithCoupon(Long memberId, Address address, List<OrderDto> orderDtos,List<Long> couponIds) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 회원이 없습니다.");
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(isMemberShip, address);
        List<OrderItem> orderItems = new ArrayList<>();

        List<Coupon> coupons = getCoupons(couponIds, member);

        getOrderItemsWithCoupons(orderDtos, coupons, isMemberShip, orderItems);

        Order order = Order.createOrder(LocalDateTime.now(), member, delivery, isMemberShip, OrderStatus.ORDER,
                orderItems);

        orderRepository.save(order);
        return order;
    }

    private List<Coupon> getCoupons(List<Long> couponIds, Member member) {
        List<Coupon> memberCoupons = couponRepository.findByMember(member);
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon memberCoupon : memberCoupons) {
            if (couponIds.contains(memberCoupon.getId())) {
                memberCoupon.checkCouponValid();
                coupons.add(memberCoupon);
            }
        }
        return coupons;
    }

    private void getOrderItemsWithCoupons(List<OrderDto> orderDtos, List<Coupon> coupons,
                                         IsMemberShip isMemberShip, List<OrderItem> orderItems) {

        for (OrderDto dto : orderDtos) {
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item == null) {
                throw new NoItemException("해당 상품이 없습니다.");
            }
            int itemPrice = item.getItemPrice();

            for (Coupon coupon : coupons) {
                // 쿠폰을 여러개 돌리면서 쿠폰이 지원하는 아이템과 같을 때 쿠폰 할인 적용
                if (coupon.getItem().getId().equals(item.getId())) {
                    itemPrice = coupon.getCouponAppliedPrice(itemPrice);
                    coupon.useCoupon();
                }
            }

            OrderItem orderItem = OrderItem.createOrderItem(item,
                    getMembershipDiscountedPrice(isMemberShip, itemPrice), dto.getItemCount());
            orderItems.add(orderItem);
        }
    }

    private Delivery getDelivery(IsMemberShip isMemberShip, Address address) {
        Delivery delivery = new Delivery(address, getDeliveryFee(isMemberShip), DeliveryStatus.READY);
        deliveryRepository.save(delivery);
        return delivery;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderByMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 멤버가 없습니다.");
        }
        return orderRepository.findByMember(member);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.orElse(null);
        if (order == null) {
            throw new NoOrderException("해당하는 주문이 없습니다.");
        }

        orderRepository.delete(order);
    }

    private int getMembershipDiscountedPrice(IsMemberShip isMemberShip, int price) {
        return discountPolicy.discount(isMemberShip, price);
    }

    private int getDeliveryFee(IsMemberShip isMemberShip) {
        if (isMemberShip == MEMBERSHIP) {
            return 0;
        } else {
            return 3000;
        }
    }
}
