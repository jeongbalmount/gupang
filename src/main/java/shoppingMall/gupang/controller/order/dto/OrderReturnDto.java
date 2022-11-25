package shoppingMall.gupang.controller.order.dto;

import lombok.Data;
import shoppingMall.gupang.domain.Order;
import shoppingMall.gupang.domain.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderReturnDto {

    // orderid, orderitems =>
    private Long orderId;
    private List<OrderItemReturnDto> orderItemDtos;

    public OrderReturnDto(Order order) {
        this.orderId = order.getId();
        this.orderItemDtos = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemReturnDto(orderItem.getItem().getName(),
                        orderItem.getItemCount(), orderItem.getItemPrice(),
                        orderItem.getCouponDiscountAmount()))
                .collect(Collectors.toList());
//        for (OrderItem orderItem : orderItems) {
//            OrderItemReturnDto dto = new OrderItemReturnDto(orderItem.getItem().getName(),
//                    orderItem.getItemCount(), orderItem.getItemPrice(),
//                    orderItem.getCouponDiscountAmount());
//            orderItemDtos.add(dto);
//        }
    }
}
