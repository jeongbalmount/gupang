package shoppingMall.gupang.discount;

import shoppingMall.gupang.domain.enums.IsMemberShip;

public interface DiscountPolicy {
    int discount(IsMemberShip isMemberShip, int price);

}
