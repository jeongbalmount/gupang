package shoppingMall.gupang.web.controller.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewEditDto {

    @NotNull
    private Long reviewId;
    @NotEmpty
    private String newTitle;
    @NotEmpty
    private String newContent;

}
