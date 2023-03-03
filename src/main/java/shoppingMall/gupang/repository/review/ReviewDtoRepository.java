package shoppingMall.gupang.repository.review;

import org.springframework.data.repository.CrudRepository;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;

import java.util.List;

public interface ReviewDtoRepository extends CrudRepository<ReviewItemDto, Long> {

    List<ReviewItemDto> findByItemIdOrderByLikeDesc(Long itemId);

    List<ReviewItemDto> findFirst5ByItemIdOrderByLikeDesc(Long itemId);

}
