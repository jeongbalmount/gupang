package shoppingMall.gupang.controller.review.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReviewDto {

    @NotNull
    private Long memberId;

    @NotNull
    private Long itemId;

    @NotNull
    private String title;

    @NotNull
    private String content;

}
