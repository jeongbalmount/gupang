package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.coupon.CouponExpiredException;
import shoppingMall.gupang.exception.coupon.NoCouponTypeException;
import shoppingMall.gupang.service.coupon.CouponService;
import shoppingMall.gupang.web.controller.coupon.dto.CouponDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
- 쿠폰 생성
    - 쿠폰 valid 테스트
        - 쿠폰 기한 확인 테스트
        - 쿠폰 종류 확인 테스트
- 멤버가 갖고 있는 유효 쿠폰 잘 리턴하는지 확인 테스트
 */

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
@Transactional
public class CouponControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CouponService couponService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private Long memberId;
    private Long itemId;

    private static final String BASE_URL = "/coupon/";

    @BeforeEach
    void init() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("mail@mail.com", "password", "memberName",
                "010-111-111", address, IsMemberShip.MEMBERSHIP);
        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");
        Item item = new Item("apple", 10000, 100, seller, category);

        em.persist(seller);
        em.persist(category);
        em.persist(item);
        em.persist(member);
        memberId = member.getId();
        itemId = item.getId();
        LocalDateTime validExpireDate =
                LocalDateTime.of(2023, 12, 30, 0, 0, 0);
        LocalDateTime notValidExpireDate =
                LocalDateTime.of(2022, 12, 12, 0, 0, 0);
        Coupon fixValidCoupon = new FixCoupon(member, item, validExpireDate, "Fix", 1000,
                "fix valid coupon");
        Coupon percentValidCoupon = new PercentCoupon(member, item, validExpireDate, "Percent",
                10, "percent valid coupon");
        Coupon fixNotValidCoupon = new FixCoupon(member, item, notValidExpireDate, "Fix", 1000,
                "fix not valid coupon");
        em.persist(fixValidCoupon);
        em.persist(percentValidCoupon);
        em.persist(fixNotValidCoupon);
    }

    @Test
    @DisplayName("회원이 갖고 있는 쿠폰 확인 테스트")
    void getMemberCouponTest() throws Exception {
        MvcResult result = mvc.perform(get(BASE_URL + memberId)
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);

        JSONObject o1 = jsonArray.getJSONObject(0);
        JSONObject o2 = jsonArray.getJSONObject(1);
        JSONObject o3 = jsonArray.getJSONObject(2);
        String[] values = {"fix valid coupon", "percent valid coupon", "fix not valid coupon"};
        Assertions.assertTrue(Arrays.asList(values).contains(o1.getString("couponName")));
        Assertions.assertTrue(Arrays.asList(values).contains(o2.getString("couponName")));
        Assertions.assertTrue(Arrays.asList(values).contains(o3.getString("couponName")));
    }


    @Test
    @DisplayName("쿠폰 생성 테스트")
    void addCouponTest() throws Exception{
        /*
        - 쿠폰 생성
            - 쿠폰 valid 테스트
                - 쿠폰 기한 확인 테스트
                - 쿠폰 종류 확인 테스트
         */
        LocalDateTime validExpireDate =
                LocalDateTime.of(2023, 12, 30, 0, 0, 0);
        LocalDateTime notValidExpireDate =
                LocalDateTime.of(2022, 12, 12, 0, 0, 0);
        CouponDto validExpireDto = new CouponDto(memberId, itemId, validExpireDate, 1000, "Fix",
                "valid expire fix coupon");
        CouponDto notValidExpireDto = new CouponDto(memberId, itemId, notValidExpireDate, 1000,
                "Fix", "not valid expire fix coupon");
        CouponDto notValidTypeDto = new CouponDto(memberId, itemId, validExpireDate, 1000,
                "Fixxx", "valid expire fix coupon");

        mvc.perform(post(BASE_URL + "add")
                        .content(mapper.writeValueAsString(validExpireDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().string("ok"));

        // 예외 검사
        mvc.perform(post(BASE_URL + "add")
                        .content(mapper.writeValueAsString(notValidExpireDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException().getClass()
                                .isAssignableFrom(CouponExpiredException.class))
                )
                .andExpect(status().is4xxClientError());

        mvc.perform(post(BASE_URL + "add")
                        .content(mapper.writeValueAsString(notValidTypeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException().getClass()
                                .isAssignableFrom(NoCouponTypeException.class))
                )
                .andExpect(status().is4xxClientError());
    }

}
