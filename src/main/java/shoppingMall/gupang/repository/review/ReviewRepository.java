package shoppingMall.gupang.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByMember(Member member);
    List<Review> findByItemAndLikeLessThan(Item item, int like);
}
