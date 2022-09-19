package shoppingMall.gupang.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.exception.NotEnoughStockException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(value = false)
public class DomainTest {

    @Autowired
    EntityManager em;

    @Test
    void cartItemTest() {
        Address address = new Address("city", "street", "zip");
        Member member = new Member("1@gmail.com", "123", "name", "010-1111-2222",
                address, IsMemberShip.NOMEMBERSHIP);
        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
        Item item = new Item("name", 100, 10, 10, seller, category);
        CartItem cartItem = new CartItem(member, item, 10, 1000);

        em.persist(member);
        em.persist(seller);
        em.persist(category);
        em.persist(item);
        em.persist(cartItem);
    }

    @Test
    void categoryTest() {
        Category category = new Category("name");
        em.persist(category);
    }

    @Test
    void deliveryTest() {
        Address address = new Address("city", "st", "zip");
        Delivery delivery = new Delivery(address, 10000, DeliveryStatus.READY);
        em.persist(delivery);
    }

    @Test
    void itemTest() {
        Seller seller = new Seller("010-1111-1111", "mn");
        Category category = new Category("cn");
        Item item = new Item("name", 1000, 10, 200, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item);
    }

    @Test
    void memberTest() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("email@gmail.com", "pwd", "name", "010-1111-1111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(member);
    }

    @Test
    void orderItemTest() {
        Seller seller = new Seller("010-1111-1111", "mn");
        Category category = new Category("cn");
        Item item = new Item("name", 1000, 10, 200, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item);
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getItemPrice(), 10);
        em.persist(orderItem);
    }

    @Test
    void orderTest() {
        Address address = new Address("city", "st", "zip");
        Delivery delivery = new Delivery(address, 3000, DeliveryStatus.READY);
        Member member = new Member("1@gmail.com", "123", "name", "010-1111-2222",
                address, IsMemberShip.NOMEMBERSHIP);
        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
        Item item = new Item("name", 100, 10, 10, seller, category);

        em.persist(delivery);
        em.persist(member);
        em.persist(seller);
        em.persist(category);
        em.persist(item);
        OrderItem orderItem1 = OrderItem.createOrderItem(item, item.getItemPrice(), 1);
        em.persist(orderItem1);
        OrderItem orderItem2 = OrderItem.createOrderItem(item, item.getItemPrice(), 2);
        em.persist(orderItem2);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        Order order = Order.createOrder(LocalDateTime.now(), delivery, IsMemberShip.NOMEMBERSHIP, OrderStatus.ORDER,
                orderItems);
        em.persist(order);
    }

    @Test
    void itemQuantityTest() {
        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
        Item item = new Item("name",100, 10, 10, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item);

        Assertions.assertThrows(NotEnoughStockException.class,
                () -> OrderItem.createOrderItem(item, item.getItemPrice(), 100));
    }


}
