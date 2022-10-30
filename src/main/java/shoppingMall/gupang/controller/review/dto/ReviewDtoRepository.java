package shoppingMall.gupang.controller.review.dto;

import org.springframework.data.repository.CrudRepository;
import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface ReviewDtoRepository extends CrudRepository<ReviewItemDto, Long> {

    List<ReviewItemDto> findReviewItemDtoByItemId(Long itemId);

}
