package shoppingMall.gupang.controller.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.service.category.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Result getCategoryItems(@RequestParam Long categoryId) {
        List<ItemReturnDto> collect = categoryService.getCategoryItems(categoryId).stream()
                .map(i -> new ItemReturnDto(i.getName(), i.getItemPrice(), i.getSeller().getManagerName(),
                        i.getCategory().getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
