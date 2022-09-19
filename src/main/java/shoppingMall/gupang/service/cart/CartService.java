package shoppingMall.gupang.service.cart;

import shoppingMall.gupang.domain.CartItem;
import java.util.List;

public interface CartService {

    List<CartItem> getAllCartItems(Long memberId);

    // 아이템 종류, 아이템 개수 => 카트 아이템 수정
    void updateItemCount(Long cartItemId, int count);

    // 장바구니 아이템 추가 => 카트 아이템, 아이템 개수
    void addCartItem(Long memberId, Long itemId, int count);

    // 장바구니 아이템 삭제 => 여러 카트 아이템
    void removeCartItems(List<Long> cartItemIds);

}
