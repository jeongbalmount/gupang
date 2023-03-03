package shoppingMall.gupang.web.controller.review.dto;

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
    private String memberEmail;
    private String title;
    private String content;
    private int like = 0;

    public String toString() {
        return "reviewID = " + reviewId + " itemId = " + itemId + " title = " + title + " content = " + content +
                " like = " + like;
    }
}
