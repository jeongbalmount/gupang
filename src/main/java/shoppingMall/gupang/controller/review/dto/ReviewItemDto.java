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
@RedisHash("reviewItemDto")
public class ReviewItemDto implements Serializable {

    @Id
    private Long reviewId;
    @Indexed
    private Long itemId;
    private String title;
    private String content;
    private int like = 0;
}
