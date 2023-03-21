package shoppingMall.gupang.domain.discount;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import shoppingMall.gupang.domain.enums.IsMemberShip;

import static java.lang.Math.max;
import static shoppingMall.gupang.domain.enums.IsMemberShip.MEMBERSHIP;

@Primary
@Component
public class FixDiscountPolicy implements DiscountPolicy{
    @Override
    public int discount(IsMemberShip isMemberShip, int price) {
        if (isMemberShip == MEMBERSHIP) {
            return Math.min(price, 3000);
        } else {
            return Math.min(price, 1000);
        }
    }
}
