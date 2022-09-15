package shoppingMall.gupang.coupon;

import java.time.LocalDateTime;

public class PercentCoupon extends Coupon{

    public PercentCoupon(Long applyItemId, LocalDateTime expireDate, int couponDiscountAmount) {
        super(applyItemId, expireDate, couponDiscountAmount);
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        return price * couponDiscountAmount / 100;
    }
}
