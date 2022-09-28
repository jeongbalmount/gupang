package shoppingMall.gupang.controller.review;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReviewDto {

    @NotEmpty
    private Long memberId;

    @NotEmpty
    private Long itemId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    public ReviewDto(Long memberId, Long itemId, String title, String content) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.title = title;
        this.content = content;
    }
}
