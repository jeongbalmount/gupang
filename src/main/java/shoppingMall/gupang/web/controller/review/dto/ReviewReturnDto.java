package shoppingMall.gupang.web.controller.review.dto;

import lombok.Data;

@Data
public class ReviewReturnDto {

    private Long reviewId;
    private String title;
    private String content;
    private boolean isMemberReview;

    private int like;

    public ReviewReturnDto(Long reviewId, String title, String content, boolean isMemberReview, int like) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.isMemberReview = isMemberReview;
        this.like = like;
    }
}
