package shoppingMall.gupang.controller.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import shoppingMall.gupang.domain.CartItem;

import java.util.List;

@Data
@AllArgsConstructor
public class CartItemsMemberDto {

    private Long memberId;
    private List<CartItemDto> cartItemIds;

}
