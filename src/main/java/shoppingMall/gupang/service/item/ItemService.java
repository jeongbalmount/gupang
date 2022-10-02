package shoppingMall.gupang.service.item;

import shoppingMall.gupang.controller.item.dto.ItemDto;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.domain.Item;

import java.util.List;

public interface ItemService {

    public void saveItem(ItemDto itemDto);

    public List<ItemReturnDto> findItemByName(String name);

}
