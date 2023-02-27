package shoppingMall.gupang.web.controller.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartItemsMemberDto {

    private Long memberId;
    private List<CartItemDto> cartItemIds;

}
