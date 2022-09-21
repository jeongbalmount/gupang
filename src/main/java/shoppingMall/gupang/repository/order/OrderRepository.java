package shoppingMall.gupang.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    List<Order> findByMember(Member member);

}
