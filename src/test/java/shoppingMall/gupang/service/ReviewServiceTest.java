package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.service.review.ReviewService;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class ReviewServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ReviewService reviewService;

    private Member member;

    private Item item;

    @BeforeEach
    void beforeEach() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("e@gmail.com", "pwd", "name", "010-1111-2222",
                address, IsMemberShip.NOMEMBERSHIP);
        em.persist(member);
        this.member = member;

        Seller seller = new Seller("010-1111-2222", "managerName");
        Category category = new Category("categoryName");
        Item item = new Item("itemName", 10000, 1000, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item);
        this.item = item;
    }



//    @Test
//    void testAddReview() {
//        ReviewDto reviewDto1 = new ReviewDto(member.getId(), item.getId(), "title", "content");
//        ReviewDto reviewDto2 = new ReviewDto(member.getId(), item.getId(), "title2", "content2");
//        reviewService.addReview(reviewDto1);
//        reviewService.addReview(reviewDto2);
//
//        List<Review> itemReviews = reviewService.getItemReviews(item.getId());
//        for (Review itemReview : itemReviews) {
//            log.info(itemReview.getContents());
//        }
//
//        log.info("==============");
//        List<Review> memberReviews = reviewService.getMemberReviews(member.getId());
//        for (Review memberReview : memberReviews) {
//            log.info(memberReview.getContents());
//        }
//
//        log.info("==============");
//        reviewService.removeReview(memberReviews.get(0).getId());
//        List<Review> memberReviews2 = reviewService.getMemberReviews(member.getId());
//        for (Review review : memberReviews2) {
//            log.info(review.getContents());
//        }
//
//    }
}
