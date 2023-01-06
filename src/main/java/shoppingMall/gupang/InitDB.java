package shoppingMall.gupang;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.review.dto.ItemReviewDto;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.loginInit();
        initService.cartInit();
        initService.couponInit();
//        initService.itemInit();
//        initService.reviewInit();
        initService.orderInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {

        private final EntityManager em;

        private Member member1;
        private Member member2;

        private Item item1;

        private Item item3;
        private Item item4;

        public void loginInit() {
            Address address1 = new Address("city1", "st1", "zip1");
            Member member1 = new Member("email1@gmail.com", "pwd1", "name1",
                    "010-1111-1111", address1, IsMemberShip.NOMEMBERSHIP);
            em.persist(member1);

            Address address2 = new Address("city2", "st2", "zip2");
            Member member2 = new Member("email2@gmail.com", "pwd2", "name2",
                    "010-2222-2222", address2, IsMemberShip.NOMEMBERSHIP);
            em.persist(member2);

            this.member1 = member1;
            this.member2 = member2;
        }

        public void cartInit() {

            Seller seller = new Seller("010-1111-1111", "managerName");
            Category category = new Category("categoryName");
            Item item1 = new Item("itemName11", 10000, 1000, seller, category);
            Item item2 = new Item("itemName22", 20000, 1000, seller, category);
            Item item3 = new Item("itemName33", 30000, 1000, seller, category);
            Item item4 = new Item("itemName44", 40000, 1000, seller, category);
            Item item5 = new Item("itemName55", 50000, 1000, seller, category);
            Item item6 = new Item("itemName66", 60000, 1000, seller, category);

            em.persist(seller);
            em.persist(category);
            em.persist(item1);
            em.persist(item2);
            em.persist(item3);
            em.persist(item4);
            em.persist(item5);
            em.persist(item6);

            this.item1 = item1;

            CartItem cartItem1 = new CartItem(member1, item1, 10, item1.getItemPrice());
            CartItem cartItem2 = new CartItem(member1, item2, 20, item2.getItemPrice());
            CartItem cartItem3 = new CartItem(member1, item3, 30, item3.getItemPrice());
            CartItem cartItem4 = new CartItem(member1, item4, 40, item4.getItemPrice());
            CartItem cartItem5 = new CartItem(member1, item6, 50, item6.getItemPrice());
            CartItem cartItem6 = new CartItem(member1, item1, 60, item1.getItemPrice());
            CartItem cartItem7 = new CartItem(member1, item2, 70, item2.getItemPrice());
            CartItem cartItem8 = new CartItem(member1, item3, 80, item3.getItemPrice());
            CartItem cartItem9 = new CartItem(member1, item4, 90, item4.getItemPrice());
            CartItem cartItem10 = new CartItem(member1, item6, 100, item6.getItemPrice());

            em.persist(cartItem1);
            em.persist(cartItem2);
            em.persist(cartItem3);
            em.persist(cartItem4);
            em.persist(cartItem5);
            em.persist(cartItem6);
            em.persist(cartItem7);
            em.persist(cartItem8);
            em.persist(cartItem9);
            em.persist(cartItem10);

        }

        public void couponInit() {
            Address address = new Address("city", "st", "zip");
            Member member = new Member("e", "p", "n", "010", address, IsMemberShip.NOMEMBERSHIP);

            Seller seller = new Seller("010", "name");
            Category category = new Category("name");
            Item item = new Item("i", 10000, 100, seller, category);

            em.persist(member);
            em.persist(seller);
            em.persist(category);
            em.persist(item);

            LocalDateTime time = LocalDateTime.of(2023, 1, 1, 10, 10);
            Coupon coupon1 = new FixCoupon(member, item, time, "Fix", 1000);
            Coupon coupon2 = new PercentCoupon(member, item, time, "Percent", 20);

            em.persist(coupon1);
            em.persist(coupon2);
        }

        public void itemInit() {
            Seller seller1 = new Seller("010-1111-1111", "managerName1");
            Seller seller2 = new Seller("010-2222-2222", "managerName2");
            Seller seller3 = new Seller("010-1111-1111", "managerName3");
            Seller seller4 = new Seller("010-2222-2222", "managerName4");
            Seller seller5 = new Seller("010-1111-1111", "managerName5");
            Seller seller6 = new Seller("010-2222-2222", "managerName6");
            Seller seller7 = new Seller("010-1111-1111", "managerName7");
            Seller seller8 = new Seller("010-2222-2222", "managerName8");
            Seller seller9= new Seller("010-1111-1111", "managerName9");
            Seller seller10 = new Seller("010-2222-2222", "managerName10");
            Seller seller11 = new Seller("010-1111-1111", "managerName11");
            Seller seller12 = new Seller("010-2222-2222", "managerName12");
            Seller seller13 = new Seller("010-1111-1111", "managerName13");
            Seller seller14 = new Seller("010-2222-2222", "managerName14");
            Seller seller15 = new Seller("010-1111-1111", "managerName15");
            em.persist(seller1);
            em.persist(seller2);
            em.persist(seller3);
            em.persist(seller4);
            em.persist(seller5);
            em.persist(seller6);
            em.persist(seller7);
            em.persist(seller8);
            em.persist(seller9);
            em.persist(seller10);
            em.persist(seller11);
            em.persist(seller12);
            em.persist(seller13);
            em.persist(seller14);
            em.persist(seller15);

            Category category1 = new Category("category1");
            Category category2 = new Category("category2");
            Category category3 = new Category("category3");
            Category category4 = new Category("category4");
            Category category5 = new Category("category5");
            Category category6 = new Category("category6");
            Category category7 = new Category("category7");
            Category category8 = new Category("category8");
            Category category9 = new Category("category9");
            Category category10 = new Category("category10");
            Category category11 = new Category("category11");
            Category category12 = new Category("category12");
            Category category13 = new Category("category13");
            Category category14 = new Category("category14");
            Category category15 = new Category("category15");
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);
            em.persist(category5);
            em.persist(category6);
            em.persist(category7);
            em.persist(category8);
            em.persist(category9);
            em.persist(category10);
            em.persist(category11);
            em.persist(category13);
            em.persist(category14);
            em.persist(category15);

            Item item7 = new Item("itemName7", 10000, 100, seller7, category7);
            Item item8 = new Item("itemName8", 10000, 100, seller8, category8);
            Item item9 = new Item("itemName9", 10000, 100, seller9, category9);
            Item item10 = new Item("itemName10", 10000, 100, seller10, category10);
            Item item11 = new Item("itemName11", 10000, 100, seller11, category11);
            Item item12 = new Item("itemName12", 10000, 100, seller12, category12);
            Item item13 = new Item("itemName13", 10000, 100, seller13, category13);
            Item item14 = new Item("itemName14", 10000, 100, seller14, category14);
            Item item15 = new Item("itemName15", 10000, 100, seller15, category15);

            em.persist(item7);
            em.persist(item8);
            em.persist(item9);
            em.persist(item10);
            em.persist(item11);
            em.persist(item12);
            em.persist(item13);
            em.persist(item14);
            em.persist(item15);

            this.item3 = item3;
            this.item4 = item4;
        }

        public void reviewInit() {
            String title1 = "title1";
            String title2 = "title2";
            String title3 = "title3";
            String title4 = "title4";
            String title5 = "title5";
            String title6 = "title6";
            String title7 = "title7";
            String title8 = "title8";
            String title9 = "title9";
            String title10 = "title10";
            String title11 = "title11";
            String title12 = "title12";
            String title13 = "title13";
            String title14 = "title14";
            String title15 = "title15";
            String title16 = "title16";

            String content1 = "content1";
            String content2 = "content2";
            String content3 = "content3";
            String content4 = "content4";

            Review review1 = new Review(item1, title1, content1);
            Review review2 = new Review(item1, title2, content2);
            Review review3 = new Review(item1, title3, content3);
            Review review4 = new Review(item1, title4, content4);
            Review review5 = new Review(item1, title5, content1);
            Review review6 = new Review(item1, title6, content2);
            Review review7 = new Review(item1, title7, content3);
            Review review8 = new Review(item1, title8, content4);
            Review review9 = new Review(item1, title9, content1);
            Review review10 = new Review(item1, title10, content2);
            Review review11 = new Review(item1, title11, content3);
            Review review12 = new Review(item1, title12, content4);
            Review review13 = new Review(item1, title13, content1);
            Review review14 = new Review(item1, title14, content2);
            Review review15 = new Review(item1, title15, content3);
            Review review16 = new Review(item1, title16, content4);

            em.persist(review1);
            em.persist(review2);
            em.persist(review3);
            em.persist(review4);
            em.persist(review5);
            em.persist(review6);
            em.persist(review7);
            em.persist(review8);
            em.persist(review9);
            em.persist(review10);
            em.persist(review11);
            em.persist(review12);
            em.persist(review13);
            em.persist(review14);
            em.persist(review15);
            em.persist(review16);
        }

        public void orderInit() {
            Address address = new Address("city", "st", "zip");
            Address address2 = new Address("city2", "st2", "zip2");

            Seller seller1 = new Seller("010-1111-1111", "managerName1");
            Seller seller2 = new Seller("010-2222-2222", "managerName2");
            em.persist(seller1);
            em.persist(seller2);

            Category category1 = new Category("category1");
            Category category2 = new Category("category2");
            em.persist(category1);
            em.persist(category2);

            Item item33 = new Item("itemName333", 10000, 10000, seller1, category1);
            Item item44 = new Item("itemName444", 10000, 10000, seller1, category1);

            em.persist(item33);
            em.persist(item44);

            Item item11 = new Item("thisnameisgood", 10000, 10000, seller1, category1);
            Item item22 = new Item("itemName222", 20000, 10000, seller1, category1);

            em.persist(item11);
            em.persist(item22);

            Delivery delivery = new Delivery(address, 3000, DeliveryStatus.READY);
            Delivery delivery2 = new Delivery(address2, 2500, DeliveryStatus.READY);
            em.persist(delivery);
            em.persist(delivery2);

            OrderItem orderItem1 = OrderItem.createOrderItem(item11, item11.getItemPrice(), 10, 0);
            OrderItem orderItem2 = OrderItem.createOrderItem(item22, item22.getItemPrice(), 20, 0);
            List<OrderItem> orderItems1 = new ArrayList<>();
            orderItems1.add(orderItem1);
            orderItems1.add(orderItem2);

            OrderItem orderItem3 = OrderItem.createOrderItem(item33, item33.getItemPrice(), 30, 0);
            OrderItem orderItem4 = OrderItem.createOrderItem(item44, item44.getItemPrice(), 40, 0);
            List<OrderItem> orderItems2 = new ArrayList<>();
            orderItems2.add(orderItem3);
            orderItems2.add(orderItem4);

            OrderItem orderItem5 = OrderItem.createOrderItem(item33, item33.getItemPrice(), 50, 0);
            OrderItem orderItem6 = OrderItem.createOrderItem(item44, item44.getItemPrice(), 60, 0);
            List<OrderItem> orderItems3 = new ArrayList<>();
            orderItems3.add(orderItem5);
            orderItems3.add(orderItem6);

            OrderItem orderItem7 = OrderItem.createOrderItem(item33, item33.getItemPrice(), 70, 0);
            OrderItem orderItem8 = OrderItem.createOrderItem(item44, item44.getItemPrice(), 80, 0);
            List<OrderItem> orderItems4 = new ArrayList<>();
            orderItems4.add(orderItem7);
            orderItems4.add(orderItem8);

            OrderItem orderItem9 = OrderItem.createOrderItem(item33, item33.getItemPrice(), 90, 0);
            OrderItem orderItem10 = OrderItem.createOrderItem(item44, item44.getItemPrice(), 100, 0);
            List<OrderItem> orderItems5 = new ArrayList<>();
            orderItems5.add(orderItem9);
            orderItems5.add(orderItem10);

            OrderItem orderItem11 = OrderItem.createOrderItem(item33, item33.getItemPrice(), 110, 0);
            OrderItem orderItem12 = OrderItem.createOrderItem(item44, item44.getItemPrice(), 120, 0);
            List<OrderItem> orderItems6 = new ArrayList<>();
            orderItems6.add(orderItem11);
            orderItems6.add(orderItem12);

            em.persist(orderItem1);
            em.persist(orderItem2);
            em.persist(orderItem3);
            em.persist(orderItem4);
            em.persist(orderItem5);
            em.persist(orderItem6);
            em.persist(orderItem7);
            em.persist(orderItem8);
            em.persist(orderItem9);
            em.persist(orderItem10);
            em.persist(orderItem11);
            em.persist(orderItem12);

            Order order1 = Order.createOrder(LocalDateTime.now(), member1, delivery, IsMemberShip.MEMBERSHIP,
                    OrderStatus.ORDER, orderItems1);

            Order order2 = Order.createOrder(LocalDateTime.now(), member1, delivery2, IsMemberShip.NOMEMBERSHIP,
                    OrderStatus.ORDER, orderItems2);

            Order order3 = Order.createOrder(LocalDateTime.now(), member1, delivery, IsMemberShip.MEMBERSHIP,
                    OrderStatus.ORDER, orderItems3);

            Order order4 = Order.createOrder(LocalDateTime.now(), member1, delivery2, IsMemberShip.NOMEMBERSHIP,
                    OrderStatus.ORDER, orderItems4);

            Order order5 = Order.createOrder(LocalDateTime.now(), member1, delivery, IsMemberShip.MEMBERSHIP,
                    OrderStatus.ORDER, orderItems5);

            Order order6 = Order.createOrder(LocalDateTime.now(), member1, delivery2, IsMemberShip.NOMEMBERSHIP,
                    OrderStatus.ORDER, orderItems6);

//            Order order7 = Order.createOrder(LocalDateTime.now(), member1, delivery, IsMemberShip.MEMBERSHIP,
//                    OrderStatus.ORDER, orderItems1);
//
//            Order order8 = Order.createOrder(LocalDateTime.now(), member1, delivery2, IsMemberShip.NOMEMBERSHIP,
//                    OrderStatus.ORDER, orderItems2);
//
//            Order order9 = Order.createOrder(LocalDateTime.now(), member1, delivery, IsMemberShip.MEMBERSHIP,
//                    OrderStatus.ORDER, orderItems1);
//
//            Order order10 = Order.createOrder(LocalDateTime.now(), member1, delivery2, IsMemberShip.NOMEMBERSHIP,
//                    OrderStatus.ORDER, orderItems2);

            em.persist(order1);
            em.persist(order2);
            em.persist(order3);
            em.persist(order4);
            em.persist(order5);
            em.persist(order6);
//            em.persist(order7);
//            em.persist(order8);
//            em.persist(order9);
//            em.persist(order10);
        }
    }
}
