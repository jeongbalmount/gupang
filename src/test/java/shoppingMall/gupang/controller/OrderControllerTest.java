package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.DeliveryCoupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.coupon.CouponExpireException;
import shoppingMall.gupang.exception.coupon.CouponExpiredException;
import shoppingMall.gupang.exception.coupon.NoCouponTypeException;
import shoppingMall.gupang.repository.order.OrderRepository;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.web.controller.order.dto.OrderItemDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/*
    - order 생성 테스트
        - 쿠폰 없는 경우 order 생성
        - 쿠폰 있는 경우 order 생성
            - 쿠폰 할인 가격 제대로 적용 됐나 확인 테스트
            - 쿠폰 종류: (퍼센트, 정량), 배달
            - 쿠폰이 valid 하지 않을 때 쿠폰 적용여부 확인 테스트
        - 상품 재고가 부족할 때 exception 테스트
    - 회원이 주문한 주문 목록 불러오기
    - order 취소 테스트
        - order DELIVERED 상태면 취소 불가
        - order 취소 후 상품 저장 수량 원복 확인
 */
@SpringBootTest
@Transactional
@Slf4j
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    private String BASE_URL = "/order";

    private Member membershipMember;
    private Member noMembershipMember;
    private Delivery delivery;
    private Item item1;
    private Item item2;
    private Item item3;

    private Long fixId;
    private Long percentId;
    private Long fixItem2Id;
    private Long percent3Id;
    private Long fixNotValidId;
    private Long deliveryCouponId;


    @BeforeEach
    void init() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        Address address = new Address("city", "st", "zip");
        Member membershipMember = new Member("memberEmail@gmail.com", "password", "name",
                "010-1111-1111", address, IsMemberShip.MEMBERSHIP);
        Member noMembershipMember = new Member("noMemberEmail@gmail.com", "password", "name",
                "010-1111-1111", address, IsMemberShip.NOMEMBERSHIP);
        Delivery delivery = new Delivery(address, 3000, DeliveryStatus.READY);
        em.persist(membershipMember);
        em.persist(noMembershipMember);
        em.persist(delivery);
        this.membershipMember = membershipMember;
        this.noMembershipMember = noMembershipMember;
        this.delivery = delivery;

        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");
        Item item1 = new Item("apple", 10000, 100, seller, category);
        Item item2 = new Item("banana", 10000, 100, seller, category);
        Item item3 = new Item("orange", 10000, 100, seller, category);

        em.persist(seller);
        em.persist(category);
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        LocalDateTime validExpireDate =
                LocalDateTime.of(2023, 12, 30, 0, 0, 0);
        LocalDateTime notValidExpireDate =
                LocalDateTime.of(2022, 12, 12, 0, 0, 0);
        Coupon fixValidCoupon = new FixCoupon(membershipMember, item1, validExpireDate, "Fix",
                10000, "fix valid coupon");
        Coupon percentValidCoupon = new PercentCoupon(membershipMember, item1, validExpireDate, "Percent",
                20, "percent valid coupon");
        Coupon fixNotValidCoupon = new FixCoupon(membershipMember, item1, notValidExpireDate, "Fix",
                1000, "fix not valid coupon");
        Coupon fixValidCouponWithItem2 = new FixCoupon(membershipMember, item2, validExpireDate, "Fix",
                9999, "fix valid coupon with item2");
        Coupon percentCouponWithItem3 = new PercentCoupon(membershipMember, item3, validExpireDate, "Fix",
                10, "percent valid coupon with item3");
        DeliveryCoupon deliveryCoupon = new DeliveryCoupon(membershipMember, validExpireDate);

        em.persist(fixValidCoupon);
        em.persist(fixNotValidCoupon);
        em.persist(percentValidCoupon);
        em.persist(fixValidCouponWithItem2);
        em.persist(percentCouponWithItem3);
        em.persist(deliveryCoupon);

        this.fixId = fixValidCoupon.getId();
        this.percentId = percentValidCoupon.getId();
        this.fixItem2Id = fixValidCouponWithItem2.getId();
        this.percent3Id = percentCouponWithItem3.getId();
        this.fixNotValidId = fixNotValidCoupon.getId();
        this.deliveryCouponId = deliveryCoupon.getId();
    }

    @Test
    @DisplayName("쿠폰이 없는 경우 주문 생성 테스트")
    void addOrderWithNoCouponTest() throws Exception {

        OrderItemDto orderItemDto1 = new OrderItemDto(item1.getId(), 10, item1.getItemPrice());
        OrderItemDto orderItemDto2 = new OrderItemDto(item2.getId(), 5, item2.getItemPrice());
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);

        OrderCouponDto orderCouponDto = OrderCouponDto.builder()
                .address(new Address("city", "st", "zip"))
                .orderItemDtos(orderItemDtos)
                .build();

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.membershipMember.getEmail());

        MvcResult result = mvc.perform(post(BASE_URL)
                        .session(mockSession)
                        .content(mapper.writeValueAsString(orderCouponDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long orderId = Long.parseLong(content);
        List<Order> orders = orderRepository.findOrderWithDelivery(orderId);
        Order order = orders.get(0);
        OrderItem orderItem = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        assertThat(orderItem.getTotalPrice()).isEqualTo(100000);
        assertThat(orderItem2.getTotalPrice()).isEqualTo(50000);
        assertThat(order.getTotalPrice()).isEqualTo(150000);
    }

    /*
        - 정량 쿠폰
            - orderItem 3개
            - 정량 쿠폰의 item id가 맞는 orderItem 가격에 제대로 할인 적용되었는지 확인
            - 1개의 orderItem에만 할인 적용 되었는지 확인
        - 정량, 퍼센트, 배송쿠폰 (정량 상품id == 퍼센트 상품id)
            - 퍼센트 쿠폰은 같은 상품이기 때문에 적용되면 안된다.
            - orderItem 3개
        - 정량, 퍼센트, 배송쿠폰 (정량 상품id != 퍼센트 상품id)
            - 정량 퍼센트 쿠폰은 맞는 orderItem에 할인이 들어가야 한다.
            - orderItem 3개
        - 정량 valid, 퍼센트 not valid, 배송쿠폰
            - 정량은 할인 적용, 퍼센트는 적용되면 안된다.
            - expired, 해당 itemid 없음,
            - orderItem 3개
        - 정량, 퍼센트 쿠폰이 상품 가격보다 크거나 100보다 클 때 예외 처리
     */
    @Test
    @DisplayName("쿠폰이 모두 valid한 상태에서 정량, 배송 쿠폰 적용 테스트")
    void allCouponsValidAndTestAllKindsOfCouponTest() throws Exception {
        OrderItemDto orderItemDto1 = new OrderItemDto(item1.getId(), 10, item1.getItemPrice());
        OrderItemDto orderItemDto2 = new OrderItemDto(item2.getId(), 10, item2.getItemPrice());
        OrderItemDto orderItemDto3 = new OrderItemDto(item3.getId(), 10, item3.getItemPrice());
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);
        orderItemDtos.add(orderItemDto3);

        List<Long> couponIds = new ArrayList<>();
        couponIds.add(fixId);

        OrderCouponDto orderCouponDto = OrderCouponDto.builder()
                .address(new Address("city", "st", "zip"))
                .couponIds(couponIds)
//                .deliveryCouponId(deliveryCouponId)
                .orderItemDtos(orderItemDtos)
                .build();

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.membershipMember.getEmail());

        MvcResult result = mvc.perform(post(BASE_URL)
                        .session(mockSession)
                        .content(mapper.writeValueAsString(orderCouponDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long orderId = Long.parseLong(content);
        List<Order> orders = orderRepository.findOrderWithDelivery(orderId);
        Order order = orders.get(0);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);
        OrderItem orderItem3 = order.getOrderItems().get(2);

        assertThat(orderItem1.getTotalPrice()).isEqualTo(90000);
        assertThat(orderItem2.getTotalPrice()).isEqualTo(100000);
        assertThat(orderItem3.getTotalPrice()).isEqualTo(100000);
        // 배송비 3000원이지만 회원 기본 3000원 할인이기 때문에 290000
        assertThat(order.getTotalPrice()).isEqualTo(290000);
    }


    @Test
    @DisplayName("같은 item id 쿠폰2개, 배송비 쿠폰 적용 테스트")
    void addOrderWithSameItemIdCouponsAndDeliveryCouponTest() throws Exception {
        OrderItemDto orderItemDto1 = new OrderItemDto(item1.getId(), 10, item1.getItemPrice());
        OrderItemDto orderItemDto2 = new OrderItemDto(item2.getId(), 10, item2.getItemPrice());
        OrderItemDto orderItemDto3 = new OrderItemDto(item3.getId(), 10, item3.getItemPrice());
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);
        orderItemDtos.add(orderItemDto3);

        List<Long> couponIds = new ArrayList<>();
        couponIds.add(percentId);
        couponIds.add(fixId);

        OrderCouponDto orderCouponDto = OrderCouponDto.builder()
                .address(new Address("city", "st", "zip"))
                .couponIds(couponIds)
                .deliveryCouponId(deliveryCouponId)
                .orderItemDtos(orderItemDtos)
                .build();

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.membershipMember.getEmail());

        MvcResult result = mvc.perform(post(BASE_URL)
                        .session(mockSession)
                        .content(mapper.writeValueAsString(orderCouponDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long orderId = Long.parseLong(content);
        List<Order> orders = orderRepository.findOrderWithDelivery(orderId);
        Order order = orders.get(0);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);
        OrderItem orderItem3 = order.getOrderItems().get(2);

        // 20% 할인 쿠폰은 item 1개의 수량에만 적용 되기 때문에 10000 * 20 / 100 + 10000 * 9 이런 식으로 계산을 해야하는 것이다.
        assertThat(orderItem1.getTotalPrice()).isEqualTo(98000);
        assertThat(orderItem2.getTotalPrice()).isEqualTo(100000);
        assertThat(orderItem3.getTotalPrice()).isEqualTo(100000);
        // 배송비 0원 회원 기본 3000원 할인
        assertThat(order.getTotalPrice()).isEqualTo(295000);
    }

    @Test
    @DisplayName("다른 item id 쿠폰2개, 배송비 쿠폰 적용 테스트")
    void addOrderWithDifferentItemIdCouponsAndDeliveryCouponTest() throws Exception {
        OrderItemDto orderItemDto1 = new OrderItemDto(item1.getId(), 10, item1.getItemPrice());
        OrderItemDto orderItemDto2 = new OrderItemDto(item2.getId(), 10, item2.getItemPrice());
        OrderItemDto orderItemDto3 = new OrderItemDto(item3.getId(), 10, item3.getItemPrice());
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);
        orderItemDtos.add(orderItemDto3);

        List<Long> couponIds = new ArrayList<>();
        couponIds.add(percentId);
        couponIds.add(fixItem2Id);

        OrderCouponDto orderCouponDto = OrderCouponDto.builder()
                .address(new Address("city", "st", "zip"))
                .couponIds(couponIds)
                .deliveryCouponId(deliveryCouponId)
                .orderItemDtos(orderItemDtos)
                .build();

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.membershipMember.getEmail());

        MvcResult result = mvc.perform(post(BASE_URL)
                        .session(mockSession)
                        .content(mapper.writeValueAsString(orderCouponDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long orderId = Long.parseLong(content);
        List<Order> orders = orderRepository.findOrderWithDelivery(orderId);
        Order order = orders.get(0);
        OrderItem orderItem1 = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);
        OrderItem orderItem3 = order.getOrderItems().get(2);

        // 20% 할인 쿠폰은 item 1개의 수량에만 적용 되기 때문에 10000 * 20 / 100 + 10000 * 9 이런 식으로 계산을 해야하는 것이다.
        assertThat(orderItem1.getTotalPrice()).isEqualTo(98000);
        assertThat(orderItem2.getTotalPrice()).isEqualTo(90001);
        assertThat(orderItem3.getTotalPrice()).isEqualTo(100000);
        // 배송비 0원 회원 기본 3000원 할인
        assertThat(order.getTotalPrice()).isEqualTo(285001);
    }
    /*
        - 정량 valid, 퍼센트 not valid, 배송쿠폰
        - 정량은 할인 적용, 퍼센트는 적용되면 안된다.
        - expired, 해당 itemid 없음,
        - orderItem 3개
     */

    // expire한 쿠폰은 item2에 적용되어햐 하지만 적용되지 않고, itemid가 없는 쿠폰은 item3에 적용되는 쿠폰이다.
    @Test
    @DisplayName("valid한 쿠폰과 not valid한 쿠폰이 같이 들어갈 때 테스트")
    void addOrderValidAndNotValidCouponsTest() throws Exception {
        OrderItemDto orderItemDto1 = new OrderItemDto(item1.getId(), 10, item1.getItemPrice());
        OrderItemDto orderItemDto2 = new OrderItemDto(item2.getId(), 10, item2.getItemPrice());
        OrderItemDto orderItemDto3 = new OrderItemDto(item3.getId(), 10, item2.getItemPrice());
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);
        orderItemDtos.add(orderItemDto3);

        // expired된 쿠폰이 있으면 정상적으로 exception 뱉어내는지 확인
        List<Long> couponIds = new ArrayList<>();
//        couponIds.add(percent3Id); // item 3
        couponIds.add(fixItem2Id); // item 2
        couponIds.add(fixNotValidId); // item 1

        OrderCouponDto orderCouponDto = OrderCouponDto.builder()
                .address(new Address("city", "st", "zip"))
                .couponIds(couponIds)
                .deliveryCouponId(deliveryCouponId)
                .orderItemDtos(orderItemDtos)
                .build();

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.membershipMember.getEmail());

        mvc.perform(post(BASE_URL)
                        .session(mockSession)
                        .content(mapper.writeValueAsString(orderCouponDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(
                result -> Assertions.assertTrue(result.getResolvedException().getClass()
                        .isAssignableFrom(CouponExpiredException.class)))
                .andExpect(status().is4xxClientError());


//        String content = result.getResponse().getContentAsString();
//        Long orderId = Long.parseLong(content);
//        List<Order> orders = orderRepository.findOrderWithDelivery(orderId);
//        Order order = orders.get(0);
//        OrderItem orderItem1 = order.getOrderItems().get(0);
//        OrderItem orderItem2 = order.getOrderItems().get(1);
//        OrderItem orderItem3 = order.getOrderItems().get(2);
//
//        // 20% 할인 쿠폰은 item 1개의 수량에만 적용 되기 때문에 10000 * 20 / 100 + 10000 * 9 이런 식으로 계산을 해야하는 것이다.
//        assertThat(orderItem1.getTotalPrice()).isEqualTo(100000);
//        assertThat(orderItem2.getTotalPrice()).isEqualTo(90001);
//        assertThat(orderItem3.getTotalPrice()).isEqualTo(100000);
//        // 배송비 0원 회원 기본 3000원 할인
//        assertThat(order.getTotalPrice()).isEqualTo(287001);

    }

}
