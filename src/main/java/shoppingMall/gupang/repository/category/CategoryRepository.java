package shoppingMall.gupang.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
