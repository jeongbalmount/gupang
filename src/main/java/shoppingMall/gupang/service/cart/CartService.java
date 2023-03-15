package shoppingMall.gupang.service.cart;

import shoppingMall.gupang.web.controller.cart.dto.CartItemIdsDto;
import shoppingMall.gupang.domain.CartItem;
import java.util.List;

public interface CartService {

    List<CartItem> getAllCartItems(String memberEmail);

    // 아이템 종류, 아이템 개수 => 카트 아이템 수정
    List<CartItem> updateItemCount(String memberEmail, Long cartItemId, int count);

    // 장바구니 아이템 추가 => 멤버 이메일, 카트 아이템, 아이템 개수
    void addCartItem(String memberEmail, Long itemId, int count);

    // 장바구니 아이템 삭제 => 여러 카트 아이템
    List<CartItem> removeCartItems(String memberEmail, CartItemIdsDto cartItemsMemberDto);

}
