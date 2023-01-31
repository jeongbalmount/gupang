package shoppingMall.gupang.domain.discount;

import org.springframework.stereotype.Component;
import shoppingMall.gupang.domain.enums.IsMemberShip;

import static shoppingMall.gupang.domain.enums.IsMemberShip.MEMBERSHIP;

@Component
public class RateDiscountPolicy implements DiscountPolicy{
    @Override
    public int discount(IsMemberShip isMemberShip, int price) {
        if (isMemberShip == MEMBERSHIP) {
            return price * 10 / 100;
        } else {
            return price * 5 / 100;
        }
    }
}
