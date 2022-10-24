package shoppingMall.gupang.controller.category;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.controller.cart.dto.CartItemMemberItemDto;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.service.category.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(name = "category", description = "카테고리 api")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "get category items", description = "카테고리 해당 상품 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ItemReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 카테고리가 존재하지 않음"),
    })
    @Parameter(name = "catetoryId", description = "카테고리 id")
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
