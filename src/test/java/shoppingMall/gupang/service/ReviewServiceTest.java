package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.review.dto.ReviewDtoRepository;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.redis.facade.RedissonLockLikeFacade;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;
import shoppingMall.gupang.service.review.ReviewService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
public class ReviewServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RedissonLockLikeFacade redissonLockLikeFacade;
    @Autowired
    private ReviewDtoRepository reviewDtoRepository;

    private Review review;
    private List<Review> reviews = new ArrayList<>();
    private Item item;


    @BeforeEach
    void beforeEach() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("e@gmail.com", "pwd", "name", "010-1111-2222",
                address, IsMemberShip.NOMEMBERSHIP);
        memberRepository.save(member);

        Seller seller = new Seller("010-1111-2222", "managerName");
        Category category = new Category("categoryName");
        Item item = new Item("itemName", 10000, 1000, seller, category);
        sellerRepository.save(seller);
        categoryRepository.save(category);
        itemRepository.save(item);
        this.item = item;

        Review review = new Review(item, "test title", "test content");
        reviewRepository.save(review);
        this.review = review;

        for (int i=0;i<5;i++){
            Review r = new Review(item, "title" + Integer.toString(i), "review" + Integer.toString(i));
            this.reviews.add(r);
        }
    }

    @AfterEach
    void after() {
        reviewDtoRepository.deleteAll();
    }

    @Test
    @DisplayName("좋아요 추가 레디스 락 없이 테스트")
    void addLikeNoLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reviewService.addLike(review.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Review review1 = reviewRepository.findById(review.getId()).orElseThrow();
        Assertions.assertThat(review1.getLike()).isEqualTo(100);
    }

    @Test
    @DisplayName("좋아요 추가 레디스 락 테스트")
    void addLikeWithLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockLikeFacade.addLike(review.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Review review1 = reviewRepository.findById(review.getId()).orElseThrow();
        Assertions.assertThat(review1.getLike()).isEqualTo(100);
    }

    @Test
    @DisplayName("리뷰 캐싱 테스트")
    void reviewCacheTest() {
        for (int i=0;i<5;i++){
            Review review = reviews.get(i);
            reviewService.addReview(new ReviewItemDto(review.getId(), review.getItem().getId(), review.getTitle(),
                    review.getContent(), review.getLike()));
        }
        Assertions.assertThat(this.reviews.size()).isEqualTo(5);
    }
}
