package shoppingMall.gupang.service.item;

import shoppingMall.gupang.web.controller.item.dto.ItemDto;
import shoppingMall.gupang.web.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.web.controller.item.dto.ItemSearchDto;

import java.util.List;

public interface ItemService {

    public Long saveItem(ItemDto itemDto);

    public List<ItemSearchDto> findItemByName(String name);

    public ItemReturnDto findSingleItem(Long itemId);

    public void decreaseQuantity(Long id, int quantity);

    public void increaseQuantity(Long id, int quantity);
}
