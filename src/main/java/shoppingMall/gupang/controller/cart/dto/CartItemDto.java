package shoppingMall.gupang.controller.cart.dto;

import lombok.Data;
import shoppingMall.gupang.domain.CartItem;
@Data
public class CartItemDto {
    private Long cartItemId;
    private int itemCount;
    private int itemPrice;
    private int totalPrice;

    private Long itemId;

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.itemCount = cartItem.getItemCount();
        this.itemPrice = cartItem.getItemPrice();
        this.totalPrice = cartItem.getItemTotalPrice();
        this.itemId = cartItem.getItem().getId();
    }
}
