package shoppingMall.gupang.controller.order.dto;

import lombok.Data;
import shoppingMall.gupang.domain.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderReturnDto {

    // orderid, orderitems =>
    private Long orderId;

    private List<OrderItemReturnDto> orderItemDtos = new ArrayList<>();

    public OrderReturnDto(Long orderId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        for (OrderItem orderItem : orderItems) {
            OrderItemReturnDto dto = new OrderItemReturnDto(orderItem.getItem().getName(),
                    orderItem.getItemCount(), orderItem.getItemPrice(),
                    orderItem.getCouponDiscountAmount());
            orderItemDtos.add(dto);
        }
    }
}
