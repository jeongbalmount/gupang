package shoppingMall.gupang.coupon;

import java.time.LocalDateTime;

public class FixCoupon extends Coupon {

    public FixCoupon(Long applyItemId, LocalDateTime expireDate, int couponDiscountAmount) {
        super(applyItemId, expireDate, couponDiscountAmount);
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        int discountedPrice = price - couponDiscountAmount;
        return Math.max(discountedPrice, 0);
    }
}
