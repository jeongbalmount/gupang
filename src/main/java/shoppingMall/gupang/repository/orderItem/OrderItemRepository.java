package shoppingMall.gupang.repository.orderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
