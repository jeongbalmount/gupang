package shoppingMall.gupang.controller.review.dto;

import lombok.Data;

@Data
public class ReviewReturnDto {

    private Long reviewId;
    private String title;
    private String content;

    public ReviewReturnDto(Long reviewId, String title, String content) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
    }
}
