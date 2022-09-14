package shoppingMall.gupang.repository.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Member;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {
}
