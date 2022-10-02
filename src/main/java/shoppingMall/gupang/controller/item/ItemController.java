package shoppingMall.gupang.controller.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.controller.item.dto.ItemDto;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.service.item.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public List<ItemReturnDto> searchItems(String itemName) {
        return itemService.findItemByName(itemName);
    }

    @PostMapping("/add")
    public String addItem(@Valid @RequestBody ItemDto itemDto) {
        itemService.saveItem(itemDto);
        return "ok";
    }


}
