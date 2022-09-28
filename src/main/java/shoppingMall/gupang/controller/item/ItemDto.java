package shoppingMall.gupang.controller.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ItemDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private int price;
    @NotEmpty
    private int quantity;
    private int discountPrice;

    @NotEmpty
    private Long seller_id;

    @NotEmpty
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
