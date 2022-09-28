package shoppingMall.gupang.service.item;

import shoppingMall.gupang.controller.item.ItemDto;
import shoppingMall.gupang.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    public void saveItem(ItemDto itemDto);

    public Item findItemByName(String name);

}
