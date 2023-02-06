package shoppingMall.gupang.repository.review;

import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findReviewsWithItem(Long reviewId);
}
