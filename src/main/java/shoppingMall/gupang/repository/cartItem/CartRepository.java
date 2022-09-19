package shoppingMall.gupang.repository.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long>, CartRepositoryCustom {
}
