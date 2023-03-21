package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

    private Long fixId;
    private Long percentId;
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
        Delivery delivery = new Delivery(address, 2000, DeliveryStatus.READY);
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

        em.persist(seller);
        em.persist(category);
        em.persist(item1);
        em.persist(item2);
        this.item1 = item1;
        this.item2 = item2;

        LocalDateTime validExpireDate =
                LocalDateTime.of(2023, 12, 30, 0, 0, 0);
        LocalDateTime notValidExpireDate =
                LocalDateTime.of(2022, 12, 12, 0, 0, 0);
        Coupon fixValidCoupon = new FixCoupon(membershipMember, item1, validExpireDate, "Fix",
                1000, "fix valid coupon");
        Coupon percentValidCoupon = new PercentCoupon(membershipMember, item1, validExpireDate, "Percent",
                10, "percent valid coupon");
        Coupon fixNotValidCoupon = new FixCoupon(membershipMember, item1, notValidExpireDate, "Fix",
                1000, "fix not valid coupon");
        DeliveryCoupon deliveryCoupon = new DeliveryCoupon(membershipMember, validExpireDate);

        em.persist(fixValidCoupon);
        em.persist(fixNotValidCoupon);
        em.persist(percentValidCoupon);
        em.persist(deliveryCoupon);

        this.fixId = fixValidCoupon.getId();
        this.percentId = percentValidCoupon.getId();
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
        - 정량, 배송
        - 퍼센트 하나만
        - 정량, 퍼센트, 배송쿠폰 (정량 상품id == 퍼센트 상품id)
            - 퍼센트 쿠폰은 같은 상품이기 때문에 적용되면 안된다.
        - 정량, 퍼센트, 배송쿠폰 (정량 상품id != 퍼센트 상품id)
     */
    @Test
    @DisplayName("쿠폰이 모두 valid한 상태에서 정량, 퍼센트, 배송 쿠폰 적용 테스트")
    void allCouponsValidAndTestAllKindsOfCouponTest() throws Exception {
        OrderItemDto orderItemDto = new OrderItemDto(item1.getId(), 10, item1.getItemPrice());
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto);

        List<Long> couponIds = new ArrayList<>();
        couponIds.add(fixId);
        couponIds.add(percentId);

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
        OrderItem orderItem = order.getOrderItems().get(0);
        OrderItem orderItem2 = order.getOrderItems().get(1);

        assertThat(orderItem.getTotalPrice()).isEqualTo(100000);
        assertThat(orderItem2.getTotalPrice()).isEqualTo(50000);
        assertThat(order.getTotalPrice()).isEqualTo(150000);
    }
}
