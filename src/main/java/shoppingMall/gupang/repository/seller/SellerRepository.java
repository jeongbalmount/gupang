package shoppingMall.gupang.repository.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {


}
