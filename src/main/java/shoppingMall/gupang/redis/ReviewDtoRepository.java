package shoppingMall.gupang.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;

import java.util.List;
@Repository
public interface ReviewDtoRepository extends CrudRepository<ReviewItemDto, String> {

    List<ReviewItemDto> findByItemId(Long itemId);

}
