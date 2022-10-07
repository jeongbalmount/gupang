package shoppingMall.gupang.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.item.dto.ItemDto;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Review;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.exception.category.NoCategoryException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.seller.NoSellerException;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

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

        Item item = new Item(itemDto.getName(), itemDto.getPrice(), itemDto.getQuantity(), seller, category);
        itemRepository.save(item);
    }

    @Override
    public List<ItemReturnDto> findItemByName(String subString) {

        List<ItemReturnDto> returnDtos = new ArrayList<>();
        List<Item> items = itemRepository.findItemByString(subString);
        for (Item item : items) {
            ItemReturnDto dto = new ItemReturnDto(item.getName(), item.getItemPrice(), item.getSeller().getManagerName(),
                    item.getCategory().getName());
            returnDtos.add(dto);
        }
        return returnDtos;
    }

    @Override
    public List<Review> getItemReviews(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        return reviewRepository.findByItem(item);
    }
}
