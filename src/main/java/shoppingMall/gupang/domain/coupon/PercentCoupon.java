package shoppingMall.gupang.domain.coupon;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class PercentCoupon extends Coupon{

    private final int percentDiscountAmount;

    public PercentCoupon(LocalDateTime expireDate, int percentDiscountAmount) {
        super(expireDate);
        this.percentDiscountAmount = percentDiscountAmount;
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        return price * percentDiscountAmount / 100;
    }
}
