package shoppingMall.gupang.controller.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;
import shoppingMall.gupang.service.review.ReviewService;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.review.ReviewController;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    <캐시 주로 사용하는 테스트>

    - 리뷰 좋아요 버튼 누르기
        - 좋아요 누른 리뷰 좋아요 1만큼 제대로 늘어 났는지 확인
        - 좋아요 누른 리뷰가 캐싱 되었다면 캐싱된 리뷰도 1만큼 늘어났는지 확인
        - 좋아요가 1만큼 늘어난 리뷰의 좋아요가 캐시의 리뷰중 가장 좋아요 수가 적은 리뷰의 좋아요보다 커진다면
          캐시에서 가장 좋아요가 적은 리뷰를 지우고 1만큼 늘어난 리뷰가 캐싱 되어야 한다.
    - 상품에 적힌 리뷰들 가져오기
        - 페이지 맞게 리뷰들 가져오기
            - 1페이지 불러올 때 캐시에서 가져오는지 테스트
            - 2페이지부터 캐시가 아닌 db에서 가져오는지 테스트
        - 캐시에 5개의 리뷰보다 적은 리뷰를 갖고 있을 때 db의 리뷰 중 가장 좋아요가 많은 리뷰 캐싱하기

 */

@SpringBootTest
@Transactional
@Slf4j
@AutoConfigureMockMvc
public class ReviewCacheControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewDtoRepository reviewDtoRepository;

    @Autowired
    private ReviewController reviewController;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/review";

    private Item item;
    private Member member;

    @BeforeEach
    void init() {
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
        em.persist(member);
        this.member = member;
    }

    // 테스트 끝나면 삭제하기
    @AfterEach
    void teardown(){
        reviewDtoRepository.deleteAll();
    }

    @Test
    @DisplayName("캐시 리뷰 추천, db 리뷰 추천 테스트")
    void reviewRecommendTest() throws Exception {
        ReviewDto reviewDto = new ReviewDto(item.getId(), "review title", "review content");
        Long reviewId = reviewService.addReview(reviewDto, member.getEmail());

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());
        mvc.perform(post(BASE_URL + "/like/" + reviewId)
                        .session(mockSession)
                ).andExpect(status().isOk());

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElse(null);
        Assertions.assertThat(review.getLike()).isEqualTo(1);

        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        Assertions.assertThat(reviewItemDto.getLike()).isEqualTo(1);
    }


    @Test
    @DisplayName("캐시 like Top5 변경 테스트")
    void top5ChangeTest() throws Exception{
        /*
            - 캐시 like top5 테스트를 위한 review 정보 만들기
            - 0부터 4까지 좋아요를 갖고 있는 리뷰 생성하고 테스트에서 새로 만들어진 리뷰가
              좋아요 수가 1이 늘어나면 좋아요 0인 리뷰가 교체되는 시나리오
        */
        for (int i=0; i < 5; i++) {
            ReviewDto reviewDto = new ReviewDto(item.getId(), "title " + String.valueOf(i),
                    "content " + String.valueOf(i));
            reviewService.addReviewForTest(reviewDto, member.getEmail(), i);
        }

        // 캐시에 좋아요 0-4 저장되어 있는지 확인
        List<ReviewItemDto> cachedReviews = reviewDtoRepository.findByItemIdOrderByLikeDesc(item.getId());
        for (int i=0;i<5;i++){
            Assertions.assertThat(cachedReviews.get(i).getLike()).isEqualTo(5-i-1);
        }

        // 새로운 리뷰 추가
        ReviewDto reviewDto = new ReviewDto(item.getId(), "new title", "new content");
        Long reviewId = reviewService.addReview(reviewDto, member.getEmail());

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, member.getEmail());

        mvc.perform(post(BASE_URL)
                .session(mockSession)
                .content(mapper.writeValueAsString(reviewDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // 새로 추가한 리뷰에 좋아요 1추가
        mvc.perform(post(BASE_URL + "/like/" + reviewId)
                .session(mockSession)
        ).andExpect(status().isOk());

        // 캐시에 좋아요 4, 3, 2, 1, 1 저장되어 있는지 확인
        List<ReviewItemDto> newCachedReviews = reviewDtoRepository.findByItemIdOrderByLikeDesc(item.getId());
        List<Integer> cachedNumbers = new ArrayList<>();
        for (int i=0;i<5;i++){
            cachedNumbers.add(newCachedReviews.get(i).getLike());
        }
        List<Integer> numbers = new ArrayList<Integer>(Arrays.asList(4, 3, 2, 1, 1));
        Assertions.assertThat(cachedNumbers.containsAll(numbers)).isTrue();
    }

    /*
        - 좋아요 수를 리뷰에 넣어서 초기화 하는게 아니라 여러개의 리뷰를 만들어 놓고
          정수를 정해서 그 정수 만큼 랜덤으로 리뷰들에 좋아요를 1씩 늘린다.
          그리고 페이징하여 1페이지 리뷰들이 캐싱되어 있는지 확인,
          이후 2, 3페이지 값들까지 좋아요 잘 정렬되어 나오는지 테스트
     */
    @Test
    @DisplayName("페이징 테스트")
    void pagingTest() {
        /*
            - 테스트를 위해 3개의 페이지를 만들기 위해서 15개의 리뷰 추가(1페이지 5개 리뷰)
            - 좋아요는 0부터 14까지 존재
         */
        for (int i=0; i < 15; i++) {
            ReviewDto reviewDto = new ReviewDto(item.getId(), "title " + String.valueOf(i),
                    "content " + String.valueOf(i));
            reviewService.addReviewForTest(reviewDto, member.getEmail(), i);
        }

        // 첫페이지의 리뷰들은 캐시에 들어간 리뷰들과 같아야 한다.
        Pageable pageable = PageRequest.of(0, 5);
        List<Integer> reviewNumbers = new ArrayList<>();
        List<Integer> cachedReviewNumbers = new ArrayList<>();
        List<ReviewReturnDto> reviews = reviewService.getItemReviews(item.getId(), member.getEmail(), pageable);
        for (ReviewReturnDto review : reviews) {
            reviewNumbers.add(review.getLike());
        }
        Iterable<ReviewItemDto> cachedReviews = reviewDtoRepository.findAll();
        for (ReviewItemDto cachedReview : cachedReviews) {
            cachedReviewNumbers.add(cachedReview.getLike());
        }
        List<Integer> answerList = new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11, 10));
        Assertions.assertThat(answerList.containsAll(reviewNumbers)).isTrue();
        Assertions.assertThat(answerList.containsAll(cachedReviewNumbers)).isTrue();
    }






}
