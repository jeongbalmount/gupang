package shoppingMall.gupang.controller.review.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReviewEditDto {

    @NotNull
    private Long reviewId;
    @NotEmpty
    private String newTitle;
    @NotEmpty
    private String newContent;

}
