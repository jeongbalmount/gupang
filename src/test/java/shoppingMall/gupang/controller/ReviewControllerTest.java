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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.review.ReviewController;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

import javax.persistence.EntityManager;

import org.springframework.web.servlet.HandlerInterceptor;
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
    private WebApplicationContext wac;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private ReviewController reviewController;


    private static final String BASE_URL = "/review";

    private Item item;
    private Member member;

    @BeforeEach
    void init() {
        /*
            - 인터셉터를 MockMvc와 같이 사용하고 싶다면 standaloneSetup의 addInterceptors를 사용하여 인터셉터를 추가하면 된다!
         */
        this.mvc = MockMvcBuilders.standaloneSetup(this.reviewController)
                .addInterceptors(this.loginInterceptor)
                .build();

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
        this.member = member;
    }

    /*
        - MockMvc를 사용하면 바로 컨트롤러 레이어로 가기 때문에
        - 세션을 확인하는 인터셉터 입장에선 새로 만들어진 엉뚱한 세션을
        - 받아드는 꼴이다.
     */
    @Test
    @DisplayName("리뷰 추가 테스트")
    void addReviewTest() throws Exception {
        ReviewDto dto = new ReviewDto(item.getId(), "the title", "contents");
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());
        MvcResult result = mvc.perform(post(BASE_URL)
                        .session(mockSession)
                        .requestAttr("mockHttpSession", mockSession)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        MockHttpSession returnMockSession = (MockHttpSession) result.getRequest().getSession();
        String loginMember = (String) returnMockSession.getAttribute(SessionConst.LOGIN_MEMBER);
        Assertions.assertThat(loginMember).isEqualTo(this.member.getEmail());
    }



}
