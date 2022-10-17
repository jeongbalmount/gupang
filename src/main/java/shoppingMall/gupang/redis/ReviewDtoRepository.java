package shoppingMall.gupang.redis;

import org.springframework.data.repository.CrudRepository;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;

import java.util.List;

public interface ReviewDtoRepository extends CrudRepository<ReviewItemDto, String> {

    List<ReviewItemDto> findByItemId(Long itemId);

}
