package shoppingMall.gupang.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.web.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.web.controller.order.dto.OrderReturnDto;

public interface OrderService {

    Long order(OrderCouponDto dto, String memberEmail);

    void cancelOrder(Long orderId);

    Page<OrderReturnDto> getOrderByMember(Long memberId, Pageable pageable);

}
