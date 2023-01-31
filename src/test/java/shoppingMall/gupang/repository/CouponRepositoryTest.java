package shoppingMall.gupang.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.coupon.CouponRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
@Rollback(value = false)
public class CouponRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void test() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("e", "p", "n", "010", address, IsMemberShip.NOMEMBERSHIP);

        Seller seller = new Seller("010", "name");
        Category category = new Category("name");
        Item item = new Item("i", 1000, 100, seller, category);

        em.persist(member);
        em.persist(seller);
        em.persist(category);
        em.persist(item);

        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 10, 10);
//        Coupon coupon1 = new FixCoupon(member, item, time, 1000);
//        Coupon coupon2 = new PercentCoupon(member, item, time, 20);
//
//        em.persist(coupon1);
//        em.persist(coupon2);
//        List<Coupon> coupons = couponRepository.findCouponByMemberWithItem(member.getId());
//        for (Coupon c : coupons) {
//            log.info(c.getMember().getName());
//            log.info(String.valueOf(c.getExpireDate()));
//            log.info(String.valueOf(c.getCouponType()));
//            log.info(String.valueOf(c.getDiscountAmount()));
//            log.info(String.valueOf(c.getItem().getItemPrice()));
//        }

    }


}
