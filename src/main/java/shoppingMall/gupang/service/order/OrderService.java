package shoppingMall.gupang.service.order;

import shoppingMall.gupang.controller.item.ItemFindDto;
import shoppingMall.gupang.coupon.Coupon;
import shoppingMall.gupang.domain.*;

import java.util.HashMap;
import java.util.List;

public interface OrderService {

    Long order(Long memberId, Address address, List<ItemFindDto> items);

    void getOrderItems(List<ItemFindDto> items, IsMemberShip isMemberShip, List<OrderItem> orderItems);

    Order orderWithCoupon(Long memberId, Address address, List<ItemFindDto> items, HashMap<Long, Coupon> couponMap);

    Delivery getDelivery(IsMemberShip isMemberShip, Address address);

    void cancelOrder(Long orderId);

    int getMembershipDiscountedPrice(IsMemberShip isMemberShip, int price);

    int getDeliveryFee(IsMemberShip isMemberShip);

}
