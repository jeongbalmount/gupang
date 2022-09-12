package shoppingMall.gupang.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findBySeller(Seller seller);
}
