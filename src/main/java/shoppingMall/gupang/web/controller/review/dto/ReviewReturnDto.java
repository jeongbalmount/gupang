package shoppingMall.gupang.web.controller.review.dto;

import lombok.Data;

@Data
public class ReviewReturnDto {

    private Long reviewId;
    private String title;
    private String content;

    private int like;

    public ReviewReturnDto(Long reviewId, String title, String content, int like) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.like = like;
    }
}
