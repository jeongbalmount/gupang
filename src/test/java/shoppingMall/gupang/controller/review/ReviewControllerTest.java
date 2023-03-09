package shoppingMall.gupang.controller.review;

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
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;
import shoppingMall.gupang.service.review.ReviewService;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.review.ReviewController;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/*
    - 새로운 리뷰 등록
    - 상품에 맞는 리뷰 불러오기
        - 첫 페이지 불러오기
        - 두번째 페이지 불러오기
    - 리뷰 삭제
    - 리뷰 수정
    - 리뷰 추천(like)
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
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewDtoRepository reviewDtoRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private ReviewController reviewController;


    private static final String BASE_URL = "/review";

    private Item item;
    private Member member;
    private Review review;
    private Long cacheReviewId;

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

        Review review = new Review(member, item, "tmp title", "tmp content");
        reviewRepository.save(review);
        this.review = review;

        /*
            - 캐시에 들어간 리뷰 삭제, 업데이트 테스트를 위한 리뷰
         */
        ReviewDto reviewDto = new ReviewDto(item.getId(), "tmp2 title", "tmp2 title");
        this.cacheReviewId = reviewService.addReview(reviewDto, member.getEmail());
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

        String content = result.getResponse().getContentAsString();
        /*
            - db와 redis에 잘 들어 갔는지 확인
         */
        Optional<Review> optionalReview = reviewRepository.findById(Long.decode(content));
        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(Long.decode(content));
        Review newReview = optionalReview.orElse(null);
        ReviewItemDto newReviewItemDto = optionalReviewItemDto.orElse(null);
        if (newReview != null) {
            log.info("11");
            Assertions.assertThat(newReview.getContent()).isEqualTo("contents");
        }
        if (newReviewItemDto != null) {
            log.info("22");
            Assertions.assertThat(newReviewItemDto.getContent()).isEqualTo("contents");
        }

    }

    /*
        - db에만 저장된 리뷰 삭제
     */
    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteReviewTest() throws Exception{
        Long reviewId = this.review.getId();
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());

        mvc.perform(delete(BASE_URL + "/" + reviewId)
                .session(mockSession)
        ).andExpect(status().isOk());

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review checkReview = optionalReview.orElse(null);
        Assertions.assertThat(checkReview).isNull();
    }
    /*
        - 캐시에도 저장된 리뷰 삭제
     */
    @Test
    @DisplayName("캐시에도 저장된 리뷰 삭제 테스트")
    void deleteInCacheReviewTest() throws Exception {
        Long reviewId = this.cacheReviewId;

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());

        mvc.perform(delete(BASE_URL + "/" + reviewId)
                .session(mockSession)
        ).andExpect(status().isOk());

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review checkReview = optionalReview.orElse(null);
        Assertions.assertThat(checkReview).isNull();

        // reviewDtoRepository => 캐시 repository
        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        Assertions.assertThat(reviewItemDto).isNull();
    }



    @Test
    @DisplayName("리뷰 수정 테스트")
    void editReviewTest() throws Exception{
        Long reviewId = this.review.getId();

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());

        ReviewEditDto reviewEditDto = new ReviewEditDto(reviewId, "newTitle", "newContent");

        mvc.perform(patch(BASE_URL)
                .session(mockSession)
                .content(mapper.writeValueAsString(reviewEditDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review newReview = optionalReview.orElse(null);
        Assertions.assertThat(newReview.getTitle()).isEqualTo("newTitle");
        Assertions.assertThat(newReview.getContent()).isEqualTo("newContent");

    }

    @Test
    @DisplayName("캐싱된 리뷰 수정 테스트")
    void editInCacheReviewTest() throws Exception{
        Long reviewId = this.cacheReviewId;

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());

        ReviewEditDto reviewEditDto = new ReviewEditDto(reviewId, "newTitle", "newContent");

        mvc.perform(patch(BASE_URL)
                .session(mockSession)
                .content(mapper.writeValueAsString(reviewEditDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review newReview = optionalReview.orElse(null);
        Assertions.assertThat(newReview.getTitle()).isEqualTo("newTitle");
        Assertions.assertThat(newReview.getContent()).isEqualTo("newContent");

        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        Assertions.assertThat(reviewItemDto.getTitle()).isEqualTo("newTitle");
        Assertions.assertThat(reviewItemDto.getContent()).isEqualTo("newContent");
    }

}
