package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.login.LoginDto;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/*
    - 새로운 리뷰 등록
    - 상품에 맞는 리뷰 불러오기
        - 첫 페이지 불러오기
        - 두번째 페이지 불러오기
    - 리뷰 삭제
    - 리뷰 수정
    - 리뷰 추천
 */

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Slf4j
public class ReviewControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private static final String BASE_URL = "/review/";

    private Item item;

    @BeforeEach
    void init() {
        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");

        em.persist(seller);
        em.persist(category);
        Item item = new Item("item name", 1000, 100, seller, category);
        em.persist(item);
        this.item = item;
        Address address = new Address("city", "st", "zip");
        Member member = new Member("test@test.com", "password", "name", "010-111-111",
                address, IsMemberShip.NOMEMBERSHIP);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("리뷰 추가 테스트")
    void addReviewTest() throws Exception {
        ReviewDto dto = new ReviewDto(item.getId(), "the title", "contents");

        LoginDto loginDto = new LoginDto("test@test.com", "password");

        MockHttpSession session = new MockHttpSession();
        // 로그인
        MvcResult result = mvc.perform(post("/login")
                .content(mapper.writeValueAsString(loginDto))
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)
        ).andReturn();

        Cookie cookie = result.getResponse().getCookie("SESSION");
        String sessionId = session.getId();
        String sessionKey = "spring:session:sessions:" + sessionId;
        String s = redisTemplate.opsForValue().get(sessionKey);

        log.info(s);

        mvc.perform(post(BASE_URL)
                        .session(session)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }



}
