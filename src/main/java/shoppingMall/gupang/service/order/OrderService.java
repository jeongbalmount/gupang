package shoppingMall.gupang.service.order;

import shoppingMall.gupang.controller.item.ItemFindDto;
import shoppingMall.gupang.controller.order.OrderDto;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface OrderService {

    Long order(Long memberId, Address address, List<OrderDto> orderDtos);

    Order orderWithCoupon(Long memberId, Address address, List<OrderDto> items, List<Long> couponIds);

    void cancelOrder(Long orderId);

    List<Order> getOrderByMember(Long memberId);

}
