package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.order.dto.OrderDto;
import shoppingMall.gupang.controller.order.dto.OrderItemDto;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.order.OrderRepository;
import shoppingMall.gupang.service.order.OrderService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
//@Rollback(value = false)
public class OrderServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private List<Item> items = new ArrayList<>();
    private Coupon fixCoupon;
    private Coupon percentCoupon;
    private Member member;

    @BeforeEach
    void beforeEach() {
        Seller seller = new Seller("010-1111-2222", "managerName");
        Category category = new Category("categoryName");
        Item item1 = new Item("itemName", 10000, 1000, seller, category);
        Item item2 = new Item("itemName", 10000, 1000, seller, category);
        Item item3 = new Item("itemName", 10000, 1000, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item1);
        em.persist(item2);// 2가 쿠폰 적용되는것
        em.persist(item3);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        Address address = new Address("city", "st", "zip");
        Member member = new Member("email@gmail.com", "pwd", "name", "010-1111-1111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(member);
        this.member = member;

        LocalDateTime expireDate = LocalDateTime.of(2022, 12, 31, 0, 0);
//        Coupon fixCoupon = new FixCoupon(member, item1, expireDate, 1000);
//        Coupon percentCoupon = new PercentCoupon(member, item2, expireDate, 20);
//
//        em.persist(fixCoupon);
//        em.persist(percentCoupon);
//
//        this.fixCoupon = fixCoupon;
//        this.percentCoupon = percentCoupon;
    }

    @Test
    void orderTest() {
        Address address = new Address("city", "st", "zip");
        long itemId = 12;
        int itemCount = 100;
        List<OrderItemDto> dtos = new ArrayList<>();
        OrderItemDto dto = new OrderItemDto();
        dto.setItemId(itemId);
        dto.setItemCount(itemCount);
        dtos.add(dto);
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setMemberId(member.getId());
        orderDto1.setCity("c");
        orderDto1.setStreet("st");
        orderDto1.setZipcode("zip");
        orderDto1.setOrderItemDtos(dtos);

        Address address1 = new Address("c", "st", "zip");
        for (Item item : items) {
            log.info(String.valueOf(item.getItemQuantity()));
            log.info(String.valueOf(item.getId()));
        }

        orderService.order(address1, orderDto1);

        List<Order> orderByMember = orderRepository.findByMemberId(member.getId());
        log.info(String.valueOf(orderByMember.size()));
        for (Order i : orderByMember) {
            log.info(String.valueOf(i.getId()));
            log.info(String.valueOf(i.getOrderItems().size()));
            for (OrderItem oi: i.getOrderItems()){
                log.info(String.valueOf(oi.getItem().getItemQuantity()));
            }
        }
        long orderNumber = 7;
        orderService.cancelOrder(orderNumber);
        List<Order> orderByMember2 = orderRepository.findByMemberId(member.getId());
        for (Order i : orderByMember2) {
            for (OrderItem oi: i.getOrderItems()){
                log.info(String.valueOf(oi.getItem().getItemQuantity()));
            }
        }
    }

//    @Test
//    void orderWithCouponTest() {
//        Address address = new Address("city", "st", "zip");
//
//        OrderDto orderDto1 = new OrderDto(items.get(0).getId(), 10, "city", "st", "zip");
//        OrderDto orderDto2 = new OrderDto(items.get(1).getId(), 20, "city", "st", "zip");
//        OrderDto orderDto3 = new OrderDto(items.get(2).getId(), 30, "city", "st", "zip");
//
//        List<OrderDto> orderDtos = new ArrayList<>();
//        orderDtos.add(orderDto1);
//        orderDtos.add(orderDto2);
//        orderDtos.add(orderDto3);
//
//        List<Long> coupons = new ArrayList<>();
//        coupons.add(this.fixCoupon.getId());
//        coupons.add(this.percentCoupon.getId());
//        orderService.orderWithCoupon(member.getId(), address, orderDtos, coupons);
//        List<Order> orderByMember = orderService.getOrderByMember(member.getId());
//    }
}
