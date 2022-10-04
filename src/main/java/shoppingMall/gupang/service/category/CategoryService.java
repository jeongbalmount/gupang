package shoppingMall.gupang.service.category;


import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface CategoryService {

    List<Item> getCategoryItems(Long categoryId);

}
