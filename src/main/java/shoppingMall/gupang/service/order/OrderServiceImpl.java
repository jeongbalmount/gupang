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
import shoppingMall.gupang.domain.coupon.DeliveryCoupon;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.order.AlreadyCanceledOrderException;
import shoppingMall.gupang.exception.order.KeyAttemptFailException;
import shoppingMall.gupang.exception.order.NoOrderException;
import shoppingMall.gupang.redis.facade.LettuceLockStockFacade;
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
    private final DeliveryCouponRepository deliveryCouponRepository;
    private final LettuceLockStockFacade lettuceLockStockFacade;

    @Override
    public Long order(Address address, OrderDto dto) {
        Optional<Member> optionalMember = memberRepository.findById(dto.getMemberId());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("?????? ????????? ????????????.");
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
                decreaseItemQuantity(dto.getItemId(), dto.getItemCount());
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            }
        }
    }

    private void decreaseItemQuantity(Long itemId, int quantity) {
        try {
            lettuceLockStockFacade.decrease(itemId, quantity);
        } catch (Exception e) {
            throw new KeyAttemptFailException("????????? ??? ?????? ???????????? ?????? ????????? ?????????????????????.");
        }
    }

    private void increaseItemQuantity(Long itemId, int quantity) {
        try {
            lettuceLockStockFacade.increase(itemId, quantity);
        } catch (Exception e) {
            throw new KeyAttemptFailException("????????? ??? ?????? ???????????? ?????? ????????? ?????????????????????.");
        }
    }

    @Override
    public Order orderWithCoupon(OrderCouponDto dto) {
        Optional<Member> optionalMember = memberRepository.findById(dto.getMemberId());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("???????????? ????????? ????????????.");
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(isMemberShip, dto.getAddress());
        List<OrderItem> orderItems = new ArrayList<>();

        List<Coupon> coupons = getCoupons(dto.getCouponIds(), member);
        List<DeliveryCoupon> deliveryCoupons = getDeliveryCoupons(dto.getDeliveryCouponIds(), member);

        getOrderItemsWithCoupons(dto.getOrderItemDtos(), coupons,deliveryCoupons, isMemberShip, orderItems);

        Order order = Order.createOrder(LocalDateTime.now(), member, delivery, isMemberShip, OrderStatus.ORDER,
                orderItems);

        orderRepository.save(order);
        return order;
    }

    private List<Coupon> getCoupons(List<Long> couponIds, Member member) {
        // ????????? ?????? ?????? ???????????? ???????????? ?????? ?????? ??????????????? coupons ???????????? ????????????
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

    private List<DeliveryCoupon> getDeliveryCoupons(List<Long> deliveryCouponIds, Member member){
        List<DeliveryCoupon> memberDeliveryCoupons = deliveryCouponRepository.findByMember(member);
        List<DeliveryCoupon> deliveryCoupons = new ArrayList<>();
        for (DeliveryCoupon memberDeliveryCoupon : memberDeliveryCoupons) {
            if (deliveryCouponIds.contains(memberDeliveryCoupon.getId())){
                memberDeliveryCoupon.checkCouponValid();
                deliveryCoupons.add(memberDeliveryCoupon);
            }
        }
        return deliveryCoupons;
    }

    private void getOrderItemsWithCoupons(List<OrderItemDto> dtos, List<Coupon> coupons,
                                          List<DeliveryCoupon> deliveryCoupons, IsMemberShip isMemberShip,
                                          List<OrderItem> orderItems) {

        for (OrderItemDto dto : dtos) {
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item == null) {
                throw new NoItemException("?????? ????????? ????????????.");
            }
            int itemCount = dto.getItemCount();
            int itemPrice = item.getItemPrice();
            int totalItemPrice = itemPrice * itemCount;
            int discountAmount = 0;

            for (Coupon coupon : coupons) {
                // ????????? ????????? ???????????? ???????????? ?????? ??????????????? ????????? ???????????? ????????? ?????? ????????? ?????? ??? ?????? ?????? ??????
                if (coupon.getItem().getId().equals(item.getId())) {
                    if (!coupon.getUsed()) {
                        discountAmount = coupon.getDiscountAmount();
                        coupon.useCoupon();
                        break;
                    }
                }
            }

            if (deliveryCoupons.size() == 0) {
                totalItemPrice += 3000;
            } else {
                for (DeliveryCoupon deliveryCoupon : deliveryCoupons) {
                    if (!deliveryCoupon.getUsed()) {
                        deliveryCoupon.useCoupon();
                        break;
                    }
                }
            }


            OrderItem orderItem = OrderItem.createOrderItem(item,
                    getMembershipDiscountedPrice(isMemberShip, totalItemPrice),
                    dto.getItemCount(), discountAmount);
            decreaseItemQuantity(dto.getItemId(), dto.getItemCount());
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
            throw new NoMemberException("?????? ????????? ????????????.");
        }
        Page<Order> page = orderRepository.findByMember(member, pageable);
        return page.map(OrderReturnDto::new);
    }

    @Override
    public void cancelOrder(Long orderId) {
        List<Order> order = orderRepository.findOrderWithDelivery(orderId);
        if (order.size() == 0) {
            throw new NoOrderException("???????????? ????????? ????????????.");
        }

        for (Order o : order) {
            if (o.getOrderStatus() == CANCEL) {
                throw new AlreadyCanceledOrderException("?????? ????????? ???????????????.");
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

    private int getDeliveryFee(IsMemberShip isMemberShip) {
        if (isMemberShip == MEMBERSHIP) {
            return 0;
        } else {
            return 3000;
        }
    }
}
