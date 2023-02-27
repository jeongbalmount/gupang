package shoppingMall.gupang.service.review;

import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;

import java.util.List;

public interface ReviewService {

    ReviewReturnDto addReview(ReviewItemDto reviewItemDto);

    List<ReviewReturnDto> getItemReviews(Long itemId);

    void removeReview(Long reviewId);

    void addLike(Long reviewId);

    void editReview(ReviewEditDto dto);

}
