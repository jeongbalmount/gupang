package shoppingMall.gupang.repository.OrderRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import shoppingMall.gupang.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QDelivery.delivery;
import static shoppingMall.gupang.domain.QItem.item;
import static shoppingMall.gupang.domain.QOrder.order;
import static shoppingMall.gupang.domain.QOrderItem.orderItem;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findOrderWithDelivery(Long orderId) {
        return queryFactory
                .selectFrom(order)
                .join(order.delivery, delivery).fetchJoin()
                .where(order.id.eq(orderId))
                .fetch();
    }

    @Override
    public List<Order> findOrderWithItems(Long orderId) {
        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(order.id.eq(orderId))
                .fetch();
    }
}
