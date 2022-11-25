package shoppingMall.gupang.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.controller.order.dto.OrderDto;
import shoppingMall.gupang.controller.order.dto.OrderItemDto;
import shoppingMall.gupang.controller.order.dto.OrderReturnDto;
import shoppingMall.gupang.discount.DiscountPolicy;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.order.AlreadyCanceledOrderException;
import shoppingMall.gupang.exception.order.AlreadyDeliveredException;
import shoppingMall.gupang.exception.order.NoOrderException;
import shoppingMall.gupang.repository.coupon.CouponRepository;
import shoppingMall.gupang.repository.delivery.DeliveryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.order.OrderRepository;
import shoppingMall.gupang.repository.orderItem.OrderItemRepository;

import java.time.LocalDateTime;
import java.util.*;

import static shoppingMall.gupang.domain.OrderStatus.CANCEL;
import static shoppingMall.gupang.domain.enums.DeliveryStatus.DELIVERED;
import static shoppingMall.gupang.domain.enums.IsMemberShip.MEMBERSHIP;

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

    @Override
    public Long order(Address address, OrderDto dto) {
        Optional<Member> optionalMember = memberRepository.findById(dto.getMemberId());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 멤버가 없습니다.");
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(member.getIsMemberShip(), address);

        List<OrderItem> orderItems = new ArrayList<>();

        getOrderItems(dto.getOrderItemDtos(), isMemberShip, orderItems);

        Order order = Order.createOrder(LocalDateTime.now(), member, delivery, isMemberShip,
                OrderStatus.ORDER, orderItems);

        orderRepository.save(order);

        return order.getId();
    }

    private void getOrderItems(List<OrderItemDto> dtos, IsMemberShip isMemberShip, List<OrderItem> orderItems) {
        for (OrderItemDto dto : dtos) {
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item != null) {
                OrderItem orderItem = OrderItem.createOrderItem(item,
                        getMembershipDiscountedPrice(isMemberShip, item.getItemPrice()),
                        dto.getItemCount(), 0);
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            }
        }
    }

    @Override
    public Order orderWithCoupon(OrderCouponDto dto) {
        Optional<Member> optionalMember = memberRepository.findById(dto.getMemberId());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 회원이 없습니다.");
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(isMemberShip, dto.getAddress());
        List<OrderItem> orderItems = new ArrayList<>();

        List<Coupon> coupons = getCoupons(dto.getCouponIds(), member);

        getOrderItemsWithCoupons(dto.getOrderItemDtos(), coupons, isMemberShip, orderItems);

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

    private void getOrderItemsWithCoupons(List<OrderItemDto> dtos, List<Coupon> coupons,
                                          IsMemberShip isMemberShip, List<OrderItem> orderItems) {

        for (OrderItemDto dto : dtos) {
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item == null) {
                throw new NoItemException("해당 상품이 없습니다.");
            }
            int itemCount = dto.getItemCount();
            int itemPrice = item.getItemPrice();
            int totalItemPrice = itemPrice * itemCount;
            int discountAmount = 0;

            for (Coupon coupon : coupons) {
                // 쿠폰을 여러개 돌리면서 사용되지 않은 쿠폰이면서 쿠폰이 지원하는 상품과 현재 상품이 같을 때 쿠폰 할인 적용
                if (coupon.getItem().getId().equals(item.getId())) {
                    if (!coupon.getUsed()) {
                        discountAmount = coupon.getDiscountAmount();
                        coupon.useCoupon();
                        break;
                    }
                }
            }

            OrderItem orderItem = OrderItem.createOrderItem(item,
                    getMembershipDiscountedPrice(isMemberShip, totalItemPrice),
                    dto.getItemCount(), discountAmount);
            orderItemRepository.save(orderItem);
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
        }
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
