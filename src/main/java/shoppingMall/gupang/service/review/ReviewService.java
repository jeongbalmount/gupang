package shoppingMall.gupang.service.review;

import org.springframework.data.domain.Pageable;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {

    void addReview(ReviewDto reviewItemDto, HttpServletRequest request);

    List<ReviewReturnDto> getItemReviews(Long itemId, HttpServletRequest request, Pageable pageable);

    void removeReview(Long reviewId, HttpServletRequest request);

    void addLike(Long reviewId);

    void editReview(ReviewEditDto dto, HttpServletRequest request);

}
