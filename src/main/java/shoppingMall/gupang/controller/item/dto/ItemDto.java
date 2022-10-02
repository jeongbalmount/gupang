package shoppingMall.gupang.controller.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {

    @NotNull
    private String name;
    @NotNull
    private int price;
    @NotNull
    private int quantity;

    @NotNull
    private Long seller_id;

    @NotNull
    private Long category_id;

}
