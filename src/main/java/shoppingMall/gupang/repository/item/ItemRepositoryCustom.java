package shoppingMall.gupang.repository.item;

import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> findItemsBySeller(Long seller_id);

    List<Item> findItemsByCategory(Long category_id);

    List<Item> findItemByString(String subString);

}
