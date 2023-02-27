package shoppingMall.gupang.service.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.web.controller.cart.dto.CartItemDto;
import shoppingMall.gupang.web.controller.cart.dto.CartItemsMemberDto;
import shoppingMall.gupang.domain.CartItem;
import java.util.List;

public interface CartService {

    List<CartItem> getAllCartItems(Long memberId);

    // 아이템 종류, 아이템 개수 => 카트 아이템 수정
    void updateItemCount(Long cartItemId, int count);

    // 장바구니 아이템 추가 => 카트 아이템, 아이템 개수
    void addCartItem(Long memberId, Long itemId, int count);

    // 장바구니 아이템 삭제 => 여러 카트 아이템
    List<CartItem> removeCartItems(CartItemsMemberDto cartItemsMemberDto);

    Page<CartItemDto> getAllCartItemsNoFetch(Long memberId, Pageable pageable);

}
