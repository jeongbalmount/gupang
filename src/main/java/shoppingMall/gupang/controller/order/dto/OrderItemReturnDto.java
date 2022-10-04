package shoppingMall.gupang.controller.order.dto;

import lombok.Data;

public class OrderItemReturnDto {
    private String itemName;
    private int itemCount;
    private int itemPrice;

    public OrderItemReturnDto(String itemName, int itemCount, int itemPrice) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
    }
}
