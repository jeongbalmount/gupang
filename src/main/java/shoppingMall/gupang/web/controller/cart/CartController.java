package shoppingMall.gupang.web.controller.cart;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import shoppingMall.gupang.service.cart.CartService;
import shoppingMall.gupang.web.consts.SessionConst;
import shoppingMall.gupang.web.controller.cart.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
                    content = @Content(schema = @Schema(implementation = CartItemReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음")
    })
    @GetMapping
    public List<CartItemReturnDto> getMemberCartItems(HttpServletRequest request){
        HttpSession session = request.getSession();
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<CartItemReturnDto> cartItemReturnDtos = cartService.getAllCartItems(memberEmail).stream()
                .map(CartItemReturnDto::new)
                .collect(Collectors.toList());

        return cartItemReturnDtos;
    }

    @Operation(summary = "add new cart item", description = "새로운 상품 카트에 넣기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 존재하지 않음"),
    })
    @Parameter(name = "itemId", description = "카트에 넣을 상품")
    @Parameter(name = "count", description = "카트에 넣을 상품 개수")
    @PostMapping
    public void addCartItem(@RequestParam("itemId") Long itemId, @RequestParam("count") int count,
                            HttpServletRequest request) {
        HttpSession session = request.getSession();
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info(String.valueOf(count));
        cartService.addCartItem(memberEmail, itemId, count);
    }

    @Operation(summary = "update cart item count", description = "카트 안에 상품 개수 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 설정한 숫자가 1보다 작음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 카트 상품의 소유자와 맞지 않음")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CartItemReturnDto.class)))
    @PutMapping
    public List<CartItemReturnDto> updateCartItemCount(@RequestParam("cartItemId") Long cartItemId,
                                                       @RequestParam("count") int count, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<CartItemReturnDto> collect = cartService.updateItemCount(memberEmail, cartItemId, count)
                .stream().map(CartItemReturnDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @Operation(summary = "delete cart item", description = "카트 상품 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CartItemReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 카트 상품의 소유자와 맞지 않음")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CartItemIdsDto.class)))
    @DeleteMapping
    public List<CartItemReturnDto> removeCartItems(@RequestBody CartItemIdsDto cartItemIdsDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<CartItemReturnDto> cartItemDtos = cartService.removeCartItems(memberEmail, cartItemIdsDto).stream()
                .map(CartItemReturnDto::new)
                .collect(Collectors.toList());

        return cartItemDtos;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
