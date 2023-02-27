package shoppingMall.gupang.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.exception.category.NoCategoryException;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.web.controller.category.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getCategoryItems(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category category = optionalCategory.orElse(null);
        if (category == null) {
            throw new NoCategoryException("해당하는 카테고리가 없습니다.");
        }

        return itemRepository.findItemsByCategory(categoryId);
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        categoryRepository.save(new Category(categoryDto.getName()));
    }
}
