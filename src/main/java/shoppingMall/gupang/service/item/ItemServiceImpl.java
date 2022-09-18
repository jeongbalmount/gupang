package shoppingMall.gupang.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.item.ItemDto;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.exception.NoCategoryException;
import shoppingMall.gupang.exception.NoSellerException;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void saveItem(ItemDto itemDto) {

        Optional<Seller> optionalSeller = sellerRepository.findById(itemDto.getSeller_id());
        Seller seller = optionalSeller.orElse(null);
        if (seller == null) {
            throw new NoSellerException("판매자를 찾을 수 없습니다.");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(itemDto.getCategory_id());
        Category category = optionalCategory.orElse(null);
        if (category == null) {
            throw new NoCategoryException("카테고리를 찾을 수 없습니다.");
        }

        Item item = new Item(itemDto.getName(), itemDto.getPrice(), itemDto.getQuantity(),
                itemDto.getDiscountPrice(), seller, category);
        itemRepository.save(item);
    }

    @Override
    public Item findItemByName(String name) {
        return itemRepository.findByName(name);
    }
}
