package shoppingMall.gupang.domain.coupon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity @Slf4j
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PercentCoupon extends Coupon{

    public PercentCoupon(Member member, Item item, LocalDateTime expireDate, String couponType,
                         int discountAmount, String couponName) {
        super(member, item, expireDate, couponType, couponName);
        super.setDiscountAmount(discountAmount);
    }

    @Override
    public int getCouponAppliedPrice(int price) {
        int percent = 100 - super.getDiscountAmount();
        return price * percent / 100;
    }

    @Override
    public int getItemDiscountedAmount(int price) {
        return price * super.getDiscountAmount() / 100;
    }


}
