package shoppingMall.gupang.service.order;

import shoppingMall.gupang.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.controller.order.dto.OrderDto;
import shoppingMall.gupang.domain.*;

import java.util.List;

public interface OrderService {

    Long order(Address address, OrderDto dto);

    Order orderWithCoupon(OrderCouponDto dto);

    void cancelOrder(Long orderId);

    List<Order> getOrderByMember(Long memberId);

}
