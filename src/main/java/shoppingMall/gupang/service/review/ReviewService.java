package shoppingMall.gupang.service.review;

import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewDto reviewDto);

    List<Review> getMemberReviews(Long MemberId);

    List<Review> getItemReviews(Long MemberId);

    void removeReview(Long reviewId);

    void addLike(Long reviewId);

    void editReview(ReviewEditDto dto);

}
