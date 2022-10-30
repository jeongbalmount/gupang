package shoppingMall.gupang.service.review;

import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface ReviewService {

    ReviewItemDto addReview(ReviewItemDto reviewItemDto);

    List<ReviewItemDto> getItemReviews(Long MemberId);

    void removeReview(Long reviewId);

    void addLike(Long reviewId);

    void editReview(ReviewEditDto dto);

}
