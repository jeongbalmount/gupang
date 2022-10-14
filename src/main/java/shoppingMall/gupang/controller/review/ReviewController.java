package shoppingMall.gupang.controller.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.service.review.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{memberId}")
    public Result getMemberReviews(@PathVariable Long memberId) {
        List<ReviewReturnDto> collect = reviewService.getMemberReviews(memberId).stream()
                .map(r -> new ReviewReturnDto(r.getId(), r.getTitle(), r.getContents()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
