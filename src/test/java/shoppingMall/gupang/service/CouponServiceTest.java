package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.service.coupon.CouponService;

import javax.persistence.EntityManager;

@SpringBootTest
@Slf4j
@Transactional
public class CouponServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CouponService couponService;

    private Member member;
    private Item item;

    @BeforeEach
    void beforeEach() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("email@gmail.com", "pwd", "name", "010-1111-1111",
                address, IsMemberShip.NOMEMBERSHIP);

        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
        Item item1 = new Item("itemName1", 1000, 10, seller, category);

        em.persist(seller);
        em.persist(category);
        em.persist(member);
        em.persist(item1);

        this.member = member;
        this.item = item1;
    }

    @Test
    void saveCouponTest() {

//        LocalDateTime expiredDate = LocalDateTime.of(2022, 9, 20, 23, 59);
//        CouponDto dto = new CouponDto(item.getId(), expiredDate, true, 1000);

//        couponService.registerCoupon(member.getId(), dto);
//        List<Coupon> unusedCoupons = couponService.getUnusedCoupons(member.getId());
//        for (Coupon i : unusedCoupons) {
//            log.info(i.toString());
//        }
    }

}
