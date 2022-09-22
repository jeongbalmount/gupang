package shoppingMall.gupang.domain.coupon;

import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class FixCoupon extends Coupon {

    private final int fixDiscountAmount;

    public FixCoupon(Member member, Item item, LocalDateTime expireDate, int fixDiscountAmount) {
        super(member, item, expireDate);
        this.fixDiscountAmount = fixDiscountAmount;
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        int discountedPrice = price - fixDiscountAmount;
        return Math.max(discountedPrice, 0);
    }
}
