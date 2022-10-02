package shoppingMall.gupang.controller.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ItemReturnDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private int price;

    @NotEmpty
    private String seller;

    @NotEmpty
    private String category;

    public ItemReturnDto(String name, int price, String seller, String category) {
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.category = category;
    }
}
