package shoppingMall.gupang.controller.review.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

//@RedisHash("reviewItemDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewItemDto implements Serializable {

    private String id;

    private Long reviewId;

    private Long itemId;

    private Long memberId;

    private String title;

    private String content;

    public ReviewItemDto(Long reviewId, Long itemId, String title, String content) {
        this.reviewId = reviewId;
        this.itemId = itemId;
        this.title = title;
        this.content = content;
    }
}
