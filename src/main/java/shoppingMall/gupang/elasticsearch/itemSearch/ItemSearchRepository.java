package shoppingMall.gupang.elasticsearch.itemSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shoppingMall.gupang.controller.item.dto.ItemSearchDto;

import java.util.List;

public interface ItemSearchRepository extends ElasticsearchRepository<ItemSearchDto, String> {
    List<ItemSearchDto> findByItemname(String name);

}
