package shoppingMall.gupang.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.web.controller.item.dto.ItemDto;
import shoppingMall.gupang.web.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.web.controller.item.dto.ItemSearchDto;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.elasticsearch.itemSearch.ItemSearchRepository;
import shoppingMall.gupang.exception.category.NoCategoryException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.seller.NoSellerException;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ItemSearchRepository itemSearchRepository;

    @Override
    public Long saveItem(ItemDto itemDto) {

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

        Item item = new Item(itemDto.getName(), itemDto.getPrice(), itemDto.getQuantity(), seller, category);
        itemRepository.save(item);
        ItemSearchDto itemSearchDto =
                new ItemSearchDto(item.getId(), item.getName(), item.getItemPrice(), item.getCategory().getName());
        itemSearchRepository.save(itemSearchDto);

        return item.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemSearchDto> findItemByName(String subString) {
        List<ItemSearchDto> items = itemSearchRepository.findByItemname(subString);
        return items;
    }

    @Override
    public ItemReturnDto findSingleItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 존재하지 않음");
        }
        return new ItemReturnDto(item.getName(), item.getItemPrice(), item.getSeller().getManagerName(),
                item.getCategory().getName(), itemId);
    }

    @Override
    public void decreaseQuantity(Long id, int quantity) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        item.removeStock(quantity);
    }

    @Override
    public void increaseQuantity(Long id, int quantity) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        item.addStock(quantity);
    }
}
