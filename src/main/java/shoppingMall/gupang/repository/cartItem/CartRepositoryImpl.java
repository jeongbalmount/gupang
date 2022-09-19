package shoppingMall.gupang.repository.cartItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import shoppingMall.gupang.domain.CartItem;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QCartItem.cartItem;
import static shoppingMall.gupang.domain.QItem.item;

public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartItem> findCartItemsByMember(Long memberId) {
        return queryFactory
                .selectFrom(cartItem)
                .join(cartItem.item, item).fetchJoin()
                .where(cartItem.member.id.eq(memberId))
                .fetch();
    }
}
