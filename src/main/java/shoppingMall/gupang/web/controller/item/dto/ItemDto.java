package shoppingMall.gupang.web.controller.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
