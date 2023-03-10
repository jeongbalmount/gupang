package shoppingMall.gupang.service.review;

import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.domain.Review;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;

import java.util.List;

public interface ReviewService {

    Long addReview(ReviewDto reviewDto, String memberEmail);

    Long addReviewForTest(ReviewDto reviewDto, String memberEmail, int like);

    List<ReviewReturnDto> getItemReviews(Long itemId, String memberEmail, Pageable pageable);

    void removeReview(Long reviewId, String request);

    void addLike(Long reviewId);

    void editReview(ReviewEditDto dto, String request);

}
