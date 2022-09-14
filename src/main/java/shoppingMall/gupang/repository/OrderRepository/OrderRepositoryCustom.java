package shoppingMall.gupang.repository.OrderRepository;

import shoppingMall.gupang.domain.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrderWithDelivery(Long orderId);

    List<Order> findOrderWithItems(Long orderId);

}
