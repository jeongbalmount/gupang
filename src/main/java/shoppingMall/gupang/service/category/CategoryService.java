package shoppingMall.gupang.service.category;


import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.web.controller.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<Item> getCategoryItems(String categoryName);
    void addCategory(CategoryDto categoryDto);

}
