package shoppingMall.gupang.controller.review.dto;

import lombok.Data;

@Data
public class ReviewReturnDto {

    private String reviewId;
    private String title;
    private String content;

    public ReviewReturnDto(String reviewId, String title, String content) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
    }
}
