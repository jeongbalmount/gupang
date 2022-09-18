package shoppingMall.gupang.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import shoppingMall.gupang.controller.item.ItemDto;
import shoppingMall.gupang.controller.item.ItemFindDto;
import shoppingMall.gupang.discount.DiscountPolicy;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.exception.NoOrderException;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.order.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static shoppingMall.gupang.domain.IsMemberShip.MEMBERSHIP;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final DiscountPolicy discountPolicy;

    @Override
    public Long order(Long memberId, Address address, List<ItemFindDto> items) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            return null;
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(isMemberShip, address);

        List<OrderItem> orderItems = new ArrayList<>();

        getOrderItems(items, isMemberShip, orderItems);

        Order order = Order.createOrder(LocalDateTime.now(), delivery, isMemberShip,
                OrderStatus.ORDER, orderItems);

        return order.getId();

    }

    @Override
    public void getOrderItems(List<ItemFindDto> items, IsMemberShip isMemberShip, List<OrderItem> orderItems) {
        for (ItemFindDto itemDto : items) {
            Optional<Item> optionalItem = itemRepository.findById(itemDto.getItemId());
            Item item = optionalItem.orElse(null);
            if (item == null) {
                return;
            }
            OrderItem orderItem = OrderItem.createOrderItem(item, itemDto.getItemCount(),
                    getMembershipDiscountedPrice(isMemberShip, item.getItemPrice()));
            orderItems.add(orderItem);
        }
    }

    @Override
    public Order orderWithCoupon(Long memberId, Address address, List<ItemFindDto> items,
                                 HashMap<Long, Coupon> couponMap) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            return null;
        }

        IsMemberShip isMemberShip = member.getIsMemberShip();

        Delivery delivery = getDelivery(isMemberShip, address);
        List<OrderItem> orderItems = new ArrayList<>();

        getOrderItemsWithCoupons(items, couponMap, isMemberShip, orderItems);

        return Order.createOrder(LocalDateTime.now(), delivery, isMemberShip, OrderStatus.ORDER, orderItems);
    }

    public void getOrderItemsWithCoupons(List<ItemFindDto> items, HashMap<Long, Coupon> couponMap,
                                         IsMemberShip isMemberShip, List<OrderItem> orderItems) {

        for (ItemFindDto ido : items) {
            Optional<Item> optionalItem = itemRepository.findById(ido.getItemId());
            Item item = optionalItem.orElse(null);
            if (item == null) {
                return;
            }
            int itemPrice = item.getItemPrice();
            if (couponMap.get(item.getId()) != null) {
                Coupon coupon = couponMap.get(item.getId());
                if (!coupon.isExpiredCoupon(coupon)) {
                    coupon.getCouponAppliedPrice(itemPrice);
                }
            }
            OrderItem orderItem = OrderItem.createOrderItem(item, ido.getItemCount(),
                    getMembershipDiscountedPrice(isMemberShip, itemPrice));
            orderItems.add(orderItem);
        }
    }

    @Override
    public Delivery getDelivery(IsMemberShip isMemberShip, Address address) {
        return new Delivery(address, getDeliveryFee(isMemberShip), DeliveryStatus.READY);
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

    @Override
    public int getMembershipDiscountedPrice(IsMemberShip isMemberShip, int price) {
        return discountPolicy.discount(isMemberShip, price);
    }

    @Override
    public int getDeliveryFee(IsMemberShip isMemberShip) {
        if (isMemberShip == MEMBERSHIP) {
            return 0;
        } else {
            return 3000;
        }
    }
}
