package shoppingMall.gupang.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
@Transactional
@Rollback(value = false)
public class CouponTest {

    @Autowired
    private EntityManager em;

    @Test
    void couponTest() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("email", "pwd", "name", "010-1111-2222", address,
                IsMemberShip.NOMEMBERSHIP);

        em.persist(member);

        Seller seller = new Seller("010-1111-1111", "mn");
        Category category = new Category("cn");
        Item item = new Item("name", 1000, 10, 200, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item);
        LocalDateTime expireDate = LocalDateTime.of(2022, 9, 19, 23, 59, 59);

        Coupon coupon = new FixCoupon(item.getId(), expireDate, 1000);
        coupon.registerCouponUser(member);
        em.persist(coupon);

    }

}
