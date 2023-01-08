package shoppingMall.gupang.service.item;

import shoppingMall.gupang.controller.item.dto.ItemDto;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.controller.item.dto.ItemSearchDto;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface ItemService {

    public void saveItem(ItemDto itemDto);

    public List<ItemSearchDto> findItemByName(String name);

    public void decreaseQuantity(Long id, int quantity);

    public void increaseQuantity(Long id, int quantity);


}
