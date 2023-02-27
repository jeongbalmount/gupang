package shoppingMall.gupang.service.category;


import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.web.controller.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<Item> getCategoryItems(Long categoryId);
    void addCategory(CategoryDto categoryDto);

}
