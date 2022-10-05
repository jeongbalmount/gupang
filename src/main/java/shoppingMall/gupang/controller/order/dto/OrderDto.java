package shoppingMall.gupang.controller.order.dto;

import lombok.Data;
import shoppingMall.gupang.domain.Address;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderDto {

    @NotNull
    private Long memberId;

    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    private String zipcode;

    @NotNull
    private List<OrderItemDto> orderItemDtos;


}
