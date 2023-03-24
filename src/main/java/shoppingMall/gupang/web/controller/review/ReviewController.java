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
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.web.consts.SessionConst;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.service.review.ReviewService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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
    @Parameter(content = @Content(schema = @Schema(implementation = ReviewDto.class)))
    @PostMapping
    public Long addReview(@RequestBody ReviewDto reviewDto, HttpSession session) {
        // 리뷰를 적은 회원은 현재 로그인한 회원이다.
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return reviewService.addReview(reviewDto, memberEmail);
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
    public String editReview(@Valid @RequestBody ReviewEditDto dto, HttpSession session){
        // review를 지울 수 있는사람은 review 작성자만 가능하기 때문에
        // 현재 접속 인원이 review 작성자인지 확인하기 위해 login 정보 세션에서 꺼내 확인
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        reviewService.editReview(dto, memberEmail);
        return "ok";
    }

    @Operation(summary = "delete review", description = "리뷰 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 리뷰가 없습니다.")
    })
    @Parameter(name = "reviewId", description = "리뷰 아이디")
    @DeleteMapping("/{reviewId}")
    public String removeReview(@PathVariable(name = "reviewId") Long reviewId, HttpSession session) {
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        reviewService.removeReview(reviewId, memberEmail);
        return "ok";
    }

    @Operation(summary = "get item reviews", description = "상품 리뷰 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 없습니다.")
    })
    @Parameter(name = "itemId", description = "상품 아이디")
    @Parameter(name = "pageable", description = "페이징을 위한 Pageable")
    @GetMapping("/item/{itemId}")
    public Result getItemReviews(@PathVariable Long itemId, HttpSession session, Pageable pageable) {
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<ReviewReturnDto> returnCollect = reviewService.getItemReviews(itemId, memberEmail, pageable);
        return new Result(returnCollect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
