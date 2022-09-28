package shoppingMall.gupang.service.seller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.exception.NoSellerException;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{

    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;

    @Override
    public void registerSeller(Long sellerId) {
        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        Seller seller = optionalSeller.orElse(null);
        if (seller == null) {
            throw new NoSellerException("해당하는 판매자가 없습니다.");
        }
        sellerRepository.save(seller);
    }

    @Override
    public void updateManagerName(Long sellerId, String name) {
        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        Seller seller = optionalSeller.orElse(null);
        if (seller == null) {
            throw new NoSellerException("해당하는 판매자가 없습니다.");
        }
        seller.updateManagerName(name);
    }

    @Override
    public List<Item> getSellerItems(Long sellerId) {
        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        Seller seller = optionalSeller.orElse(null);
        if (seller == null) {
            throw new NoSellerException("해당하는 판매자가 없습니다.");
        }
        return itemRepository.findBySeller(seller);
    }
}
