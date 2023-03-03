package shoppingMall.gupang.repository.review;

import shoppingMall.gupang.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryCustom {
    List<Review> findReviewsWithItem(Long reviewId);

    List<Review> findReviewWithMember(Long reviewId);

    List<Review> findReviewsWithLikeLessThanWithMember(int like);
}
