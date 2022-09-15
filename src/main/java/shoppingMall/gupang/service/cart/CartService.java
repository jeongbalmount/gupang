package shoppingMall.gupang.service.cart;

import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface CartService {

    List<CartItem> getAllCartItems(Long MemberId);

    // 아이템 종류, 아이템 개수 => 카트 아이템 수정
    void updateItemCount(Item item, int count);

    // 장바구니 아이템 추가 => 카트 아이템, 아이템 개수
    void addCartItem(Item ite, int count);

    // 장바구니 아이템 삭제 => 여러 카트 아이템
    void removeCartItems(List<CartItem> cartItems);

}
