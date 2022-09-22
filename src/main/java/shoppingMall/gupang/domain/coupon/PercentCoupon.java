package shoppingMall.gupang.domain.coupon;

import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class PercentCoupon extends Coupon{

    private final int percentDiscountAmount;

    public PercentCoupon(Member member, Item item, LocalDateTime expireDate, int percentDiscountAmount) {
        super(member, item, expireDate);
        this.percentDiscountAmount = percentDiscountAmount;
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        return price * percentDiscountAmount / 100;
    }
}
