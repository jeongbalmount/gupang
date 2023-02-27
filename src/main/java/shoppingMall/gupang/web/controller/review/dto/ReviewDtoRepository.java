package shoppingMall.gupang.web.controller.review.dto;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewDtoRepository extends CrudRepository<ReviewItemDto, Long> {

    List<ReviewItemDto> findByItemIdOrderByLikeDesc(Long itemId);

    List<ReviewItemDto> findFirst5ByItemIdOrderByLikeDesc(Long itemId);

}
