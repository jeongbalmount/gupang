package shoppingMall.gupang.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.order.OrderRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
@Slf4j
public class OrderRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    private List<OrderItem> orderItems = new ArrayList<>();

    private List<Item> items = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        Seller seller = new Seller("010-1111-1111", "mn");
        Category category = new Category("ct");
        Item item1 = new Item("name",10000, 200, seller, category);
        Item item2 = new Item("name",10000, 300, seller, category);
        Item item3 = new Item("name",10000, 300, seller, category);

        em.persist(seller);
        em.persist(category);
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getItemPrice(), 1000, 0);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getItemPrice(), 2000,0);
        OrderItem orderItem3 = OrderItem.createOrderItem(item3, item3.getItemPrice(), 3000,0);
        em.persist(orderItem1);
        em.persist(orderItem2);
        em.persist(orderItem3);

        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        orderItems.add(orderItem3);
        Address address = new Address("city", "st", "zip");
        Delivery delivery = new Delivery(address, 1000, DeliveryStatus.READY);

        Member member = new Member("e@gmail.com", "pwd", "name", "010-1111-2222",
                address, IsMemberShip.NOMEMBERSHIP);
        em.persist(member);
//        int totalPrice = 0;
//        for (int i = 1; i < 4; i++) {
//            OrderItem
//        }
//        order = Order.createOrder(LocalDateTime.now(), member, delivery, IsMemberShip.NOMEMBERSHIP, OrderStatus.ORDER,
//                this.orderItems);
//        em.persist(delivery);
//        em.persist(order);
    }

    @Test
    void orderRepositoryTest() {
        // 로그에 update가 뜨는 이유는 createOrder에서 orderItem 추가 및 orderItem에 order 등록하는 과정 때문
        List<Order> orders = orderRepository.findOrderWithDelivery(order.getId());
        Order order = orders.get(0);
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem oi : orderItems) {
            log.info(String.valueOf(oi.getItemPrice()));
        }

        orderRepository.findOrderWithDelivery(order.getId());
    }
}
