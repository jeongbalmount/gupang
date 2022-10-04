package shoppingMall.gupang.controller.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemDto {
    @NotNull
    private Long itemId;
    @NotNull
    private int itemCount;
}
