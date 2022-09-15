package shoppingMall.gupang.coupon;

import java.time.LocalDateTime;

public abstract class Coupon {

    Long applyItemId;
    LocalDateTime expireDate;
    int couponDiscountAmount;

    public Coupon(Long applyItemId, LocalDateTime expireDate, int couponDiscountAmount) {

        this.applyItemId = applyItemId;
        this.expireDate = expireDate;
        this.couponDiscountAmount = couponDiscountAmount;
    }

    public Coupon() {

    }

    public abstract int getCouponAppliedPrice(int price);

    public boolean isExpiredCoupon(Coupon coupon){
        if (expireDate.isBefore(LocalDateTime.now())) {
            return false;
        } else {
            return true;
        }
    };

}
