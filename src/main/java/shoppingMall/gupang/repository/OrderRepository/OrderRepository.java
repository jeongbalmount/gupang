package shoppingMall.gupang.repository.OrderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
