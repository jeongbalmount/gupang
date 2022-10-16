package shoppingMall.gupang.redis;

import org.springframework.data.repository.CrudRepository;
import shoppingMall.gupang.controller.review.dto.ItemReviewDto;

public interface ItemRedisRepository extends CrudRepository<ItemReviewDto, Long> {
}
