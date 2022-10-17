package shoppingMall.gupang.controller.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("reviewItemDto")
@Data
public class ReviewItemDto implements Serializable {
    @Id
    private String id;

    @Indexed
    private Long itemId;

    private String title;

    private String content;

}
