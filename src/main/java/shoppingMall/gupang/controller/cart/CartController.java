package shoppingMall.gupang.controller.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.cart.dto.*;
import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.service.cart.CartService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j
public class CartController {
    private final CartService cartService;

    @PostMapping
    public Result getMemberCartItems(@RequestBody CartItemMemberDto cartItemMemberDto){
        List<CartItemDto> cartItemDtos = cartService.getAllCartItems(cartItemMemberDto.getMemberId()).stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());

        return new Result(cartItemDtos);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @PostMapping("/add")
    public void addCartItem(@RequestBody CartItemMemberItemDto cartItemMemberItemDto) {
        cartService.addCartItem(cartItemMemberItemDto.getMemberId(), cartItemMemberItemDto.getItemId(),
                cartItemMemberItemDto.getCount());
    }

    @PatchMapping
    public void updateCartItemCount(@RequestBody CartItemCountDto cartItemCountDto) {
        cartService.updateItemCount(cartItemCountDto.getCartItemId(), cartItemCountDto.getUpdateCount());
    }

    @DeleteMapping
    public Result removeCartItems(@RequestBody CartItemsMemberDto cartItemsMemberDto) {

        List<CartItemDto> cartItemDtos = cartService.removeCartItems(cartItemsMemberDto).stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());

        return new Result(cartItemDtos);
    }


}
