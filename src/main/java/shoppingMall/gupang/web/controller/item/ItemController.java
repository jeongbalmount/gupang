package shoppingMall.gupang.web.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.web.controller.item.dto.ItemDto;
import shoppingMall.gupang.web.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.web.controller.item.dto.ItemSearchDto;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.service.item.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @Operation(summary = "search items", description = "이름에 맞는 상품 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ItemReturnDto.class))),
    })
    @Parameter(name = "itemName", description = "상품 이름")
    @GetMapping("/{itemName}")
    public List<ItemSearchDto> searchItems(@PathVariable(name = "itemName") String itemName) {
        return itemService.findItemByName(itemName);
    }

    @Operation(summary = "add item", description = "상품 추가하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 판매자 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 카테고리 존재하지 않음"),
    })
    @Parameter(content = @Content(schema = @Schema(implementation = ItemDto.class)))
    @PostMapping("/add")
    public String addItem(@Valid @RequestBody ItemDto itemDto) {
        itemService.saveItem(itemDto);
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
