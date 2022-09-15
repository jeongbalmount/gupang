package shoppingMall.gupang.controller.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ItemDto {

    // int itemPrice, int itemQuantity, int discountPrice, Seller seller_id, Category category_id
    private int price;
    private int quantity;
    private int discountPrice;
    private Long seller_id;
    private Long category_id;

    public ItemDto(int price, int quantity, int discountPrice, Long seller_id, Long category_id) {
        this.price = price;
        this.quantity = quantity;
        this.discountPrice = discountPrice;
        this.seller_id = seller_id;
        this.category_id = category_id;
    }
}
