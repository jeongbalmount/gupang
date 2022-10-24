package shoppingMall.gupang.controller.cart;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "cart", description = "카트 API")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "get member cart items", description = "멤버의 카트 물품 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CartItemDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CartItemMemberDto.class)))
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

    @Operation(summary = "add new cart item", description = "새로운 상품 카트에 넣기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 존재하지 않음")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CartItemMemberItemDto.class)))
    @PostMapping("/add")
    public void addCartItem(@RequestBody CartItemMemberItemDto cartItemMemberItemDto) {
        cartService.addCartItem(cartItemMemberItemDto.getMemberId(), cartItemMemberItemDto.getItemId(),
                cartItemMemberItemDto.getCount());
    }

    @Operation(summary = "update cart item count", description = "카트 안에 상품 개수 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 존재하지 않음")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CartItemMemberItemDto.class)))
    @PatchMapping
    public void updateCartItemCount(@RequestBody CartItemCountDto cartItemCountDto) {
        cartService.updateItemCount(cartItemCountDto.getCartItemId(), cartItemCountDto.getUpdateCount());
    }

    @Operation(summary = "delete cart item", description = "카트 상품 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CartItemDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 존재하지 않음")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CartItemsMemberDto.class)))
    @DeleteMapping
    public Result removeCartItems(@RequestBody CartItemsMemberDto cartItemsMemberDto) {

        List<CartItemDto> cartItemDtos = cartService.removeCartItems(cartItemsMemberDto).stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());

        return new Result(cartItemDtos);
    }


}
