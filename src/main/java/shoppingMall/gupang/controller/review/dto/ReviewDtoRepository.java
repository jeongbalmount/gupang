package shoppingMall.gupang.controller.review.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface ReviewDtoRepository extends CrudRepository<ReviewItemDto, Long> {

    List<ReviewItemDto> findByItemIdOrderByLikeDesc(Long itemId);

    List<ReviewItemDto> findFirst5ByItemIdOrderByLikeDesc(Long itemId);

}
