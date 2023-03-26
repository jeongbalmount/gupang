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
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.redis.facade.RedissonLockLikeFacade;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;
import shoppingMall.gupang.service.review.ReviewService;
import shoppingMall.gupang.web.consts.SessionConst;
import shoppingMall.gupang.web.controller.review.ReviewController;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    <캐시 테스트>

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
    - 좋아요 락 테스트(Redisson)

 */

@SpringBootTest
//@Transactional
@Slf4j
@AutoConfigureMockMvc
public class ReviewCacheControllerTest {

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

    @Autowired
    private RedissonLockLikeFacade redissonLockLikeFacade;

    private static final String BASE_URL = "/review";

    private Item item;
    private Member member;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;


    @BeforeEach
    void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(this.reviewController)
                .addInterceptors(this.loginInterceptor)
                .build();

        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");

        sellerRepository.save(seller);
        categoryRepository.save(category);
        Item item = new Item("item name", 1000, 100, seller, category);
        itemRepository.save(item);

        this.item = item;
        Address address = new Address("city", "st", "zip");
        Member member = new Member("test@test.com", "password", "name", "010-111-111",
                address, IsMemberShip.NOMEMBERSHIP);
        memberRepository.save(member);
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

        List<Long> reviewIds = new ArrayList<>();
        for (int i=0; i < 15; i++) {
            ReviewDto reviewDto = new ReviewDto(item.getId(), "title " + String.valueOf(i),
                    "content " + String.valueOf(i));
            Long reviewId = reviewService.addReviewForTest(reviewDto, member.getEmail(), i);
            reviewIds.add(reviewId);
        }

        // 좋아요 100을 15개의 리뷰에 랜덤으로 배분
        int LIKES = 100;
        for (int i=0; i< LIKES; i++) {
            Long randomReviewId = getRandomListValue(reviewIds);
            reviewService.addLike(randomReviewId);
        }

        // 좋아요 잘 배분 됐나 확인 코드
        List<Review> distributeReviews = reviewRepository.findAllByOrderByLikeDesc();
        for (Review review : distributeReviews) {
            log.info(String.valueOf(review.getLike()));
        }

        // 첫페이지의 리뷰들은 캐시에 들어간 리뷰들과 같아야 한다.
        Pageable pageable = PageRequest.of(0, 5);
        List<Integer> reviewNumbers = new ArrayList<>();
        List<Integer> cachedReviewNumbers = new ArrayList<>();
        List<ReviewReturnDto> reviews = reviewService.getItemReviews(item.getId(), member.getEmail(), pageable);
        for (ReviewReturnDto review : reviews) {
            reviewNumbers.add(review.getLike());
        }
        Iterable<ReviewItemDto> cachedReviews = reviewDtoRepository.findByItemIdOrderByLikeDesc(item.getId());
        for (ReviewItemDto cachedReview : cachedReviews) {
            cachedReviewNumbers.add(cachedReview.getLike());
        }

        log.info(String.valueOf(reviewNumbers));
        log.info(String.valueOf(cachedReviewNumbers));
        Assertions.assertThat(reviewNumbers.containsAll(cachedReviewNumbers)).isTrue();
    }

    // id 리스트에서 랜덤으로 값을 한개 빼내는 함수
    private static Long getRandomListValue(List<Long> list) {
        Random random = new Random();

        if (list == null || list.isEmpty()) {
            return null;
        }

        int randomIndex = random.nextInt(list.size());

        return list.get(randomIndex);
    }

    /*
        - test의 @Transactional을 끄지 않으면 service 계층에서 값이 업데이트 되지 않는다.
        - 이 문제는 트랜잭션의 격리 수준 때문에 발생할 수 있다. mysql의 기본적인 트랜잭션 격리 수준은 REPEATABLE READ이다.
          이는 하나의 트랜잭션에서 변경된 데이터가 커밋되어야만 다른 트랜잭션에서 조회할 수 있다는 것을 의미한다.
          따라서, 테스트 코드의 @Transactional이 붙어 있을 경우에는,
          테스트 코드 내에서 Review 엔티티의 like 값이 증가하더라도 해당 트랜잭션이 커밋되기 전까지는 다른 트랜잭션에서 조회할 수 없다.
          이에 따라, RedissonLockLikeFacade 클래스에서 addLike 메소드를 호출하더라도 해당 리뷰 엔티티의 like 값이 갱신되지 않아서,
          테스트가 실패하게 된다. 반면에, 테스트 코드의 @Transactional을 주석 처리하면, 해당 테스트 코드의 트랜잭션이 존재하지 않기 때문에
          Review 엔티티의 like 값이 즉시 증가하게 된다. 따라서, RedissonLockLikeFacade 클래스에서 addLike 메소드를 호출해도
          해당 리뷰 엔티티의 like 값이 즉시 갱신되기 때문에, 테스트가 성공하게 된다.
          즉, 테스트에서 해당 review를 트랜잭션으로 잡고 있기 때문에 테스트의 transaction이 끝나기 전까지는 그 리뷰를
          조회하지 못해서 like가 늘어나지 않는다.
     */
    @Test
    @DisplayName("좋아요 추가 레디스 락 테스트(Redisson)")
    void addLikeWithLock() throws InterruptedException {

        ReviewDto reviewDto = new ReviewDto(item.getId(), "title", "content");
        Long newReviewId = reviewService.addReview(reviewDto, member.getEmail());

        int COUNT = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(COUNT);
        for (int i = 0; i < COUNT; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockLikeFacade.addLike(newReviewId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Review review1 = reviewRepository.findById(newReviewId).orElseThrow();
        Assertions.assertThat(review1.getLike()).isEqualTo(100);
    }
}
