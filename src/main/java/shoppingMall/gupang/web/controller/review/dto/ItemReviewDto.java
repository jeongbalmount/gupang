package shoppingMall.gupang.web.controller.review.dto;

import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.List;

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
