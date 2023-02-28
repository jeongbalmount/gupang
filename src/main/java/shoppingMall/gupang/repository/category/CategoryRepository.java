package shoppingMall.gupang.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

}
