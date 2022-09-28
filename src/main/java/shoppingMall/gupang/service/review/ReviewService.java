package shoppingMall.gupang.service.review;

import shoppingMall.gupang.controller.review.ReviewDto;
import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewDto reviewDto);

    List<Review> getMemberReviews(Long MemberId);

    List<Review> getItemReviews(Long MemberId);

    void removeReview(Long reviewId);

}
