package shoppingMall.gupang.controller.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.service.review.ReviewService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public String addReview(@RequestBody ReviewDto reviewDto) {
        reviewService.addReview(reviewDto);
        return "ok";
    }

    @PostMapping("/like")
    public String addReviewLike(Long reviewId){
        reviewService.addLike(reviewId);
        return "ok";
    }

    @PatchMapping
    public String editReview(@Valid @RequestBody ReviewEditDto reviewEditDto){
        reviewService.editReview(reviewEditDto);
        return "ok";
    }

    @DeleteMapping
    public String removeReview(Long reviewId) {
        reviewService.removeReview(reviewId);
        return "ok";
    }

}
