package shoppingMall.gupang.controller.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ItemDto {

    private String name;
    private int price;
    private int quantity;
    private int discountPrice;
    private Long seller_id;
    private Long category_id;

    public ItemDto(String name, int price, int quantity, int discountPrice, Long seller_id, Long category_id) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discountPrice = discountPrice;
        this.seller_id = seller_id;
        this.category_id = category_id;
    }
}
