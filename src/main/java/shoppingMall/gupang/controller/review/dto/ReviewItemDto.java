package shoppingMall.gupang.controller.review.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("reviewItemDto")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewItemDto implements Serializable {
    @Id
    private String id;

    @Indexed
    private Long itemId;

    private Long memberId;

    private String title;

    private String content;

    public ReviewItemDto(Long itemId, String title, String content) {
        this.itemId = itemId;
        this.title = title;
        this.content = content;
    }
}
