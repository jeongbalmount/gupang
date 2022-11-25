package shoppingMall.gupang.repository.cartItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Member;

public interface CartRepository extends JpaRepository<CartItem, Long>, CartRepositoryCustom {

    Page<CartItem> findByMember(Member member, Pageable pageable);

}
