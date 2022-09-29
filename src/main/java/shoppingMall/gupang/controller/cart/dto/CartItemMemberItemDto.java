package shoppingMall.gupang.controller.cart.dto;

import lombok.Data;

@Data
public class CartItemMemberItemDto {
    private Long memberId;
    private Long itemId;
    private int count;
}
