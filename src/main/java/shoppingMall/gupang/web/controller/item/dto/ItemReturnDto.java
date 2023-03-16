package shoppingMall.gupang.web.controller.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemReturnDto {

    @NotEmpty
    private String itemName;

    @NotEmpty
    private int itemPrice;

    @NotEmpty
    private String sellerName;

    @NotEmpty
    private String categoryName;

    @NotEmpty
    private Long itemId;

    public ItemReturnDto(String name, int price, String sellerName, String categoryName, Long itemId) {
        this.itemName = name;
        this.itemPrice = price;
        this.sellerName = sellerName;
        this.categoryName = categoryName;
        this.itemId = itemId;
    }
}
