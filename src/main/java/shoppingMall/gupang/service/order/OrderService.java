package shoppingMall.gupang.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.web.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.web.controller.order.dto.OrderDto;
import shoppingMall.gupang.web.controller.order.dto.OrderReturnDto;
import shoppingMall.gupang.domain.*;

public interface OrderService {

    Long order(Address address, OrderDto dto);

    Order orderWithCoupon(OrderCouponDto dto);

    void cancelOrder(Long orderId);

    Page<OrderReturnDto> getOrderByMember(Long memberId, Pageable pageable);

}
