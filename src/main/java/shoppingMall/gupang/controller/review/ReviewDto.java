package shoppingMall.gupang.controller.review;

import lombok.Data;

@Data
public class ReviewDto {

    private Long memberId;
    private Long itemId;
    private String title;
    private String content;

    public ReviewDto(Long memberId, Long itemId, String title, String content) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.title = title;
        this.content = content;
    }
}
