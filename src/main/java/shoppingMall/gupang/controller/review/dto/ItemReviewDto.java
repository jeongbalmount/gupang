package shoppingMall.gupang.controller.review.dto;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("item")
@Getter
public class ItemReviewDto {

    @Id
    private Long itemId;

    private String dtoName;
    private List<ReviewDto> dtos;

    public ItemReviewDto(String name) {
        this.dtoName = name;
    }

    public void addDto(ReviewDto dto) {
        this.dtos.add(dto);
    }
}
