package shoppingMall.gupang.web.controller.order.dto;

import lombok.Data;
import shoppingMall.gupang.domain.Address;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderCouponDto {

    @NotNull
    private Long memberId;

    @NotNull
    private Address address;

    @NotNull
    private List<OrderItemDto> orderItemDtos;

    @NotNull
    private List<Long> couponIds;

    @NotNull
    private List<Long> deliveryCouponIds;

}
