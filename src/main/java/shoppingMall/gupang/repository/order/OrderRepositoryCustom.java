package shoppingMall.gupang.repository.order;

import shoppingMall.gupang.domain.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrderWithDelivery(Long orderId);

    List<Order> findOrderWithMember(Long memberId);

}
