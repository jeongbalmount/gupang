package shoppingMall.gupang.repository.review;

import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findReviewsWithItem(Long reviewId);

    List<Review> findReviewWithMember(Long reviewId);

    List<Review> findReviewsWithLikeLessThanWithMember(Item item, int like, Pageable pageable);

    List<Review> findReviewWithMemberWithPage(Item item, Pageable pageable);
}
