package shoppingMall.gupang.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
