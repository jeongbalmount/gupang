package shoppingMall.gupang.controller.order.dto;

import lombok.Data;

@Data
public class OrderItemReturnDto {
    private String itemName;
    private int itemCount;
    private int itemPrice;
    private int discountAmount;

    public OrderItemReturnDto(String itemName, int itemCount, int itemPrice, int discountAmount) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.discountAmount = discountAmount;
    }
}
