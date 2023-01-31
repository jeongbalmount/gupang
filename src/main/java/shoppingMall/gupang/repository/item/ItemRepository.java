package shoppingMall.gupang.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findByName(String itemName);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select i from Item i where i.id = :id")
    Optional<Item> findByIdWithPessimisticLock(@Param("id") Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query(value = "select i from Item i where i.id = :id")
    Optional<Item> findByIdWithOptimisticLock(@Param("id") Long id);
}
