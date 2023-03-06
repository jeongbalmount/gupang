package shoppingMall.gupang.redis;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.repository.review.ReviewRepository;
import shoppingMall.gupang.service.review.ReviewService;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class ReviewRedisRepositoryTest {

    @Autowired
    private ReviewDtoRepository reviewDtoRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager em;

    private Item theItem;

    private Member member;

    @BeforeEach
    void before() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("mail", "password", "name", "010-111-111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(member);
        this.member = member;

        Item item = makeItem();
        theItem = item;

        List<Review> reviews = makeReviews(item);

        ReviewItemDto reviewItemDto1 = new ReviewItemDto(reviews.get(0).getId(), item.getId(),
                member.getEmail(), reviews.get(0).getTitle(), reviews.get(0).getContent(), reviews.get(0).getLike());
        ReviewItemDto reviewItemDto2 = new ReviewItemDto(reviews.get(1).getId(), item.getId(),
                member.getEmail(),reviews.get(1).getTitle(), reviews.get(1).getContent(), reviews.get(1).getLike());
        ReviewItemDto reviewItemDto3 = new ReviewItemDto(reviews.get(2).getId(), item.getId(),
                member.getEmail(),reviews.get(2).getTitle(), reviews.get(2).getContent(), reviews.get(2).getLike());
        ReviewItemDto reviewItemDto4 = new ReviewItemDto(reviews.get(3).getId(), item.getId(),
                member.getEmail(),reviews.get(3).getTitle(), reviews.get(3).getContent(), reviews.get(3).getLike());
        ReviewItemDto reviewItemDto5 = new ReviewItemDto(reviews.get(4).getId(), item.getId(),
                member.getEmail(),reviews.get(4).getTitle(), reviews.get(4).getContent(), reviews.get(4).getLike());

        reviewDtoRepository.save(reviewItemDto1);
        reviewDtoRepository.save(reviewItemDto2);
        reviewDtoRepository.save(reviewItemDto3);
        reviewDtoRepository.save(reviewItemDto4);
        reviewDtoRepository.save(reviewItemDto5);
    }

    @AfterEach
    void after() {
        reviewDtoRepository.deleteAll();
    }

    @Test
    void getAllData() {
        Iterable<ReviewItemDto> reviews = reviewDtoRepository.findAll();
        for (ReviewItemDto reviewDto : reviews) {
            log.info(reviewDto.toString());
        }
    }

    @Test
    void addNewItem(){
        List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(theItem.getId());
        log.info(String.valueOf(reviewDtos.size()));
        String newTitle = "newTitle";
        String newContent = "newContent";
        Review newReview = new Review(member, theItem, newTitle, newContent);
        em.persist(newReview);
        ReviewDto dto = new ReviewDto(theItem.getId(), newTitle, newContent);

        //HttpServletRequest mock으로 넣어줘야함
//        HttpServletRequest request = new HttpServletRequest();
//        reviewService.addReview(dto, HttpServletRequest request);
        List<ReviewItemDto> reviewDtos2 = reviewDtoRepository.findByItemIdOrderByLikeDesc(theItem.getId());
        for (ReviewItemDto reviewItemDto : reviewDtos2) {
            log.info(reviewItemDto.getTitle());

        }
    }

    @Test
    void reviewLikeTest(){
        List<ReviewItemDto> reviews = reviewDtoRepository.findByItemIdOrderByLikeDesc(theItem.getId());
        for (ReviewItemDto review : reviews) {
            log.info(review.getContent());
            log.info(String.valueOf(review.getLike()));
        }
    }

    @Test
    void findByItem() {
        List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(theItem.getId());
        int dbReviewDtoCount = 5 - reviewDtos.size();
        int lessNum = 1000;
        if (dbReviewDtoCount > 0) {
            for (ReviewItemDto dto : reviewDtos) {
                if (dto.getLike() < lessNum) {
                    lessNum = dto.getLike();
                }
            }
            List<Review> leftReviews = reviewRepository.findByItemAndLikeLessThan(theItem, lessNum);

            for (Review r : leftReviews) {
                ReviewItemDto newDto = new ReviewItemDto(r.getId(),
                        r.getItem().getId(), member.getEmail(), r.getTitle(), r.getContent(), r.getLike());
                reviewDtoRepository.save(newDto);
                reviewDtos.add(newDto);
            }
        }

        Assertions.assertThat(5).isEqualTo(reviewDtos.size());
        for (ReviewItemDto reviewDto : reviewDtos) {
            log.info(reviewDto.toString());
        }
    }

    @Test
    void getTop5() {
        List<ReviewItemDto> reviews = reviewDtoRepository.findFirst5ByItemIdOrderByLikeDesc(theItem.getId());
        for (ReviewItemDto reviewDto : reviews) {
            log.info(reviewDto.toString());
        }
    }

    @Test
    void page5() {
        Pageable pageable = PageRequest.of(1, 5);
        List<ReviewItemDto> reviews = reviewDtoRepository.findByItemIdOrderByLikeDesc(theItem.getId());
        for (ReviewItemDto reviewItemDto : reviews) {
            log.info(reviewItemDto.toString());
        }
    }

    public Item makeItem() {
        Seller seller1 = new Seller("010-1111-1111", "managerName1");
        em.persist(seller1);

        Category category1 = new Category("category1");
        em.persist(category1);

        Item item = new Item("item", 10000, 100, seller1, category1);
        em.persist(item);

        return item;
    }

    public List<Review> makeReviews(Item item1) {
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

        String content1 = "content1";
        String content2 = "content2";
        String content3 = "content3";
        String content4 = "content4";
        String content5 = "content5";
        String content6 = "content6";
        String content7 = "content7";
        String content8 = "content8";
        String content9 = "content9";
        String content10 = "content10";

        Review review1 = new Review(member, item1, title1, content1);
        Review review2 = new Review(member, item1, title2, content2);
        Review review3 = new Review(member, item1, title3, content3);
        Review review4 = new Review(member, item1, title4, content4);
        Review review5 = new Review(member, item1, title5, content5);
        Review review6 = new Review(member, item1, title6, content6);
        Review review7 = new Review(member, item1, title7, content7);
        Review review8 = new Review(member, item1, title8, content8);
        Review review9 = new Review(member, item1, title9, content9);
        Review review10 = new Review(member, item1, title10, content10);
        review1.setLike(13);
        review2.setLike(223);
        review3.setLike(32);
        review4.setLike(464);
        review5.setLike(51);
        review6.setLike(66);
        review7.setLike(713);
        review8.setLike(84);
        review9.setLike(93);
        review10.setLike(1011);

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

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        reviews.add(review4);
        reviews.add(review5);
        reviews.add(review6);
        reviews.add(review7);
        reviews.add(review8);
        reviews.add(review9);
        reviews.add(review10);
        return reviews;
    }


}
