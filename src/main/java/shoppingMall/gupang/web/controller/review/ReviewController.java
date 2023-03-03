package shoppingMall.gupang.web.controller.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.service.review.ReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
@Tag(name = "review", description = "리뷰 api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "add review", description = "리뷰 더하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 없습니다.")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = ReviewItemDto.class)))
    @PostMapping
    public String addReview(@RequestBody ReviewItemDto reviewItemDto, HttpServletRequest request) {
        ReviewReturnDto dto = reviewService.addReview(reviewItemDto, request);
        return "ok";

    }

    @Operation(summary = "add like", description = "리뷰 좋아요 더하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 리뷰가 없습니다.")
    })
    @Parameter(name = "reviewId", description = "리뷰 아이디")
    @PostMapping("/like/{reviewId}")
    public String addReviewLike(@PathVariable Long reviewId){
        reviewService.addLike(reviewId);
        return "ok";
    }

    @Operation(summary = "edit review", description = "리뷰 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 리뷰가 없습니다."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 수정하려는 리뷰는 제목과 내용이 있어야 합니다.")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = ReviewEditDto.class)))
    @PatchMapping
    public String editReview(@Valid @RequestBody ReviewEditDto dto, HttpServletRequest request){
        reviewService.editReview(dto, request);
        return "ok";
    }

    @Operation(summary = "delete review", description = "리뷰 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 리뷰가 없습니다.")
    })
    @Parameter(name = "reviewId", description = "리뷰 아이디")
    @DeleteMapping("/{reviewId}")
    public String removeReview(@PathVariable Long reviewId, HttpServletRequest request) {
        reviewService.removeReview(reviewId, request);
        return "ok";
    }

    @Operation(summary = "get item reviews", description = "상품 리뷰 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 없습니다.")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = ReviewItemDto.class)))
    @GetMapping("/item/{itemId}")
    public Result getItemReviews(@PathVariable Long itemId) {
        List<ReviewReturnDto> returnCollect = reviewService.getItemReviews(itemId)
                .stream()
                .map(rd -> new ReviewReturnDto(rd.getReviewId(), rd.getTitle(), rd.getContent(), rd.getLike()))
                .collect(Collectors.toList());

        return new Result(returnCollect);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
