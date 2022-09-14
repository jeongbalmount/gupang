package shoppingMall.gupang.repository.cartItem;

import shoppingMall.gupang.domain.CartItem;

import java.util.List;

public interface CartItemRepositoryCustom {

    List<CartItem> findCartItemsByMember(Long memberId);

}
