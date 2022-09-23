package shoppingMall.gupang.domain.coupon;

import lombok.extern.slf4j.Slf4j;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity @Slf4j
public class PercentCoupon extends Coupon{

    private final int percentDiscountAmount;

    public PercentCoupon(Member member, Item item, LocalDateTime expireDate, int percentDiscountAmount) {
        super(member, item, expireDate);
        this.percentDiscountAmount = percentDiscountAmount;
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        log.info("pppoo");
        log.info(String.valueOf(price));
        int percent = 100 - percentDiscountAmount;
        return price * percent / 100;
    }
}
