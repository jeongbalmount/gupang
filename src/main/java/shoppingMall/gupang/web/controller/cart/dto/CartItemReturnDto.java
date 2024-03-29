package shoppingMall.gupang.web.controller.cart.dto;

import lombok.Data;
import shoppingMall.gupang.domain.CartItem;
@Data
public class CartItemReturnDto {

    private Long itemId;
    private String itemName;
    private int itemCount;
    private int itemPrice;

    public CartItemReturnDto(CartItem cartItem) {
        this.itemId = cartItem.getItem().getId();
        this.itemName = cartItem.getItem().getName();
        this.itemCount = cartItem.getItemCount();
        this.itemPrice = cartItem.getItemPrice();
    }
}
