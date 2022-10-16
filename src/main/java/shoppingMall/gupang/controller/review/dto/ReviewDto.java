package shoppingMall.gupang.controller.review.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ReviewDto implements Serializable {

    @NotNull
    private Long memberId;

    @NotNull
    private Long itemId;

    @NotNull
    private String title;

    @NotNull
    private String content;

}
