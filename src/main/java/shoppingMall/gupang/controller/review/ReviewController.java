package shoppingMall.gupang.controller.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.service.review.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public String addReview(@RequestBody ReviewItemDto reviewItemDto) {
        reviewItemDto.setId(UUID.randomUUID().toString());
        ReviewItemDto dto = reviewService.addReview(reviewItemDto);
        return "ok";

    }

    @PostMapping("/like")
    public String addReviewLike(Long reviewId){
        reviewService.addLike(reviewId);
        return "ok";
    }

    @PatchMapping
    public String editReview(@Valid @RequestBody ReviewEditDto dto){
        reviewService.editReview(dto);
        return "ok";
    }

    @DeleteMapping
    public String removeReview(Long reviewId) {
        reviewService.removeReview(reviewId);
        return "ok";
    }

//    @GetMapping("/{memberId}")
//    public Result getMemberReviews(@PathVariable Long memberId) {
//        List<ReviewReturnDto> collect = reviewService.getMemberReviews(memberId).stream()
//                .map(r -> new ReviewReturnDto(r.getId(), r.getTitle(), r.getContents()))
//                .collect(Collectors.toList());
//
//        return new Result(collect);
//    }

    @GetMapping("/item/{itemId}")
    public Result getItemReviews(@PathVariable Long itemId) {
        List<ReviewItemDto> collect = reviewService.getItemReviews(itemId);
        log.info(collect.get(0).getContent());
        List<ReviewReturnDto> returnCollect = collect.stream()
                .map(rd -> new ReviewReturnDto(rd.getReviewId(), rd.getTitle(), rd.getContent()))
                .collect(Collectors.toList());

        return new Result(returnCollect);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
