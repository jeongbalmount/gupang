package shoppingMall.gupang.service.review;

import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {

    ReviewReturnDto addReview(ReviewItemDto reviewItemDto, HttpServletRequest request);

    List<ReviewReturnDto> getItemReviews(Long itemId);

    void removeReview(Long reviewId, HttpServletRequest request);

    void addLike(Long reviewId);

    void editReview(ReviewEditDto dto, HttpServletRequest request);

}
