package shoppingMall.gupang.controller.cart.dto;

import lombok.Data;
import shoppingMall.gupang.domain.CartItem;

import java.util.List;

@Data
public class CartItemsMemberDto {

    private Long memberId;
    private List<CartItemsDto> cartItemIds;

}
