package shoppingMall.gupang.service.order;

import shoppingMall.gupang.web.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.web.controller.order.dto.OrderReturnDto;

import java.util.List;

public interface OrderService {

    Long order(OrderCouponDto dto, String memberEmail);

    void cancelOrder(Long orderId);

    List<OrderReturnDto> getOrderByMember(Long memberId);

}
