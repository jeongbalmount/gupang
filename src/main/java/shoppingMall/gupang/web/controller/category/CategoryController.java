package shoppingMall.gupang.web.controller.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.web.controller.category.dto.CategoryDto;
import shoppingMall.gupang.web.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.service.category.CategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(name = "category", description = "카테고리 api")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "get category items", description = "카테고리 해당 상품 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ItemReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 카테고리가 존재하지 않음"),
    })
    @Parameter(name = "catetoryId", description = "카테고리 id")
    @GetMapping("/{categoryId}")
    public Result getCategoryItems(@PathVariable Long categoryId) {
        List<ItemReturnDto> collect = categoryService.getCategoryItems(categoryId).stream()
                .map(i -> new ItemReturnDto(i.getName(), i.getItemPrice(), i.getSeller().getManagerName(),
                        i.getCategory().getName(), i.getId()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping("/add")
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
//        categoryService.addCategory(categoryDto);
        return categoryDto;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
