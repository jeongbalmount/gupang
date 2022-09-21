package shoppingMall.gupang.domain.coupon;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class FixCoupon extends Coupon {

    private final int fixDiscountAmount;

    public FixCoupon(LocalDateTime expireDate, int fixDiscountAmount) {
        super(expireDate);
        this.fixDiscountAmount = fixDiscountAmount;
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        int discountedPrice = price - fixDiscountAmount;
        return Math.max(discountedPrice, 0);
    }
}
