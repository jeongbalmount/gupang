package shoppingMall.gupang.web.controller.order.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemDto {
    @NotNull
    private Long itemId;

    @NotNull
    private int itemCount;

    @NotNull
    private int itemPrice;
}
