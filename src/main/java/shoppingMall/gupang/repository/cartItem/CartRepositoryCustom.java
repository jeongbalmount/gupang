package shoppingMall.gupang.repository.cartItem;

import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Member;

import java.util.List;

public interface CartRepositoryCustom {
    List<CartItem> findCartItemsByMemberId(Long memberId);
}
