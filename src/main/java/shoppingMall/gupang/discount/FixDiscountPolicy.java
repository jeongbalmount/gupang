package shoppingMall.gupang.discount;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import shoppingMall.gupang.domain.IsMemberShip;

import static java.lang.Math.max;
import static shoppingMall.gupang.domain.IsMemberShip.MEMBERSHIP;

@Primary
@Component
public class FixDiscountPolicy implements DiscountPolicy{
    @Override
    public int discount(IsMemberShip isMemberShip, int price) {
        int discountPrice;

        if (isMemberShip == MEMBERSHIP) {
            discountPrice = price - 3000;
        } else {
            discountPrice = price - 1000;
        }

        return max(discountPrice, 0);
    }
}
