package shoppingMall.gupang.web.controller.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CartItemIdsDto {

    List<Long> cartItemIds = new ArrayList<>();

}
