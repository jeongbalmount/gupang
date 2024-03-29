package shoppingMall.gupang.service.seller;

import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.web.controller.item.dto.ItemDto;

import java.util.List;

public interface SellerService {

    void registerSeller(String managerName, String sellerNumber);

    void updateManagerName(Long sellerId, String name);

    List<Item> getSellerItems(Long sellerId);
}
