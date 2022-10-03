package shoppingMall.gupang.repository.item;

import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> findItemsBySeller(String name);

}
