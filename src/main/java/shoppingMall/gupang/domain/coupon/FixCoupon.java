package shoppingMall.gupang.domain.coupon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FixCoupon extends Coupon {

    public FixCoupon(Member member, Item item, LocalDateTime expireDate, String couponType, int discountAmount) {
        super(member, item, expireDate, couponType);
        super.setDiscountAmount(discountAmount);
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        int discountedPrice = price - super.getDiscountAmount();
        return Math.max(discountedPrice, 0);
    }

    @Override
    public int getItemDiscountedAmount(int price) {
        return super.getDiscountAmount();
    }


}
