package shoppingMall.gupang.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.review.ReviewRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager em;

//    @Test
//    void testFindByMember(){
//        Address address = new Address("city", "st", "zip");
//        Member member = new Member("email", "pwd", "name", "010-1111-1111, ",
//                address, IsMemberShip.NOMEMBERSHIP);
//
//        Member member2 = new Member("email", "pwd", "name", "010-1111-1111, ",
//                address, IsMemberShip.NOMEMBERSHIP);
//
//        em.persist(member);
//        em.persist(member2);
//
//        Seller seller = new Seller("010-1111-1111", "mn");
//        Category category = new Category("cn");
//        Item item = new Item("name",10, 200, seller, category);
//        Item item2 = new Item("name",10, 200, seller, category);
//        em.persist(seller);
//        em.persist(category);
//        em.persist(item);
//        em.persist(item2);
//
//        Review review = new Review(member, item, "title", "contents");
//        Review review2 = new Review(member, item, "title2", "contents2");
//
//        Review review3 = new Review(member, item, "title", "contents3");
//        Review review4 = new Review(member, item, "title2", "contents4");
//
//        em.persist(review);
//        em.persist(review2);
//        em.persist(review3);
//        em.persist(review4);
//
//        log.info("hello!");
//        List<Review> reviews = reviewRepository.findByMember(member);
//        for (Review r : reviews) {
//            log.info(r.getContents());
//        }
//
//    }

}
