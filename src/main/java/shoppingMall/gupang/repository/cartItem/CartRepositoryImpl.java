package shoppingMall.gupang.repository.cartItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import shoppingMall.gupang.domain.CartItem;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QCartItem.cartItem;
import static shoppingMall.gupang.domain.QItem.item;
import static shoppingMall.gupang.domain.QMember.member;

public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CartRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;

    }

    @Override
    public List<CartItem> findCartItemsByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(cartItem)
                .join(cartItem.member, member).fetchJoin()
                .where(member.id.eq(memberId))
                .fetch();
    }

}
