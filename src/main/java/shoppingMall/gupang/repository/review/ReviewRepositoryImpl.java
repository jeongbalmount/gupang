package shoppingMall.gupang.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Review;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QItem.item;
import static shoppingMall.gupang.domain.QMember.member;
import static shoppingMall.gupang.domain.QReview.*;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> findReviewsWithItem(Long reviewId) {
        return queryFactory
                .selectFrom(review)
                .join(review.item, item).fetchJoin()
                .where(review.id.eq(reviewId))
                .fetch();
    }

    @Override
    public List<Review> findReviewWithMember(Long reviewId) {
        return queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .where(review.id.eq(reviewId))
                .fetch();
    }

    @Override
    public List<Review> findReviewsWithLikeLessThanWithMember(Item item, int like, Pageable pageable) {

        return queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .where(review.item.eq(item)
                        .and((review.like.loe(like))))
                .orderBy(review.like.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Review> findReviewWithMemberWithPage(Item item, Pageable pageable) {

        return queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .where(review.item.eq(item))
                .orderBy(review.like.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
