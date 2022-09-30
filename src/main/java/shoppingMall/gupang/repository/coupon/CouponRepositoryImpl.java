package shoppingMall.gupang.repository.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import shoppingMall.gupang.domain.QItem;
import shoppingMall.gupang.domain.QMember;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.QCoupon;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QItem.item;
import static shoppingMall.gupang.domain.QMember.member;
import static shoppingMall.gupang.domain.coupon.QCoupon.coupon;

public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public CouponRepositoryImpl(EntityManager em) {
        entityManager = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Coupon> findCouponByMemberWithItem(Long memberId) {
        return queryFactory
                .selectFrom(coupon)
                .join(coupon.item, item).fetchJoin()
                .where(coupon.member.id.eq(memberId))
                .fetch();
    }

}
