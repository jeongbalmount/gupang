package shoppingMall.gupang.discount;

import shoppingMall.gupang.domain.IsMemberShip;

public interface DiscountPolicy {

    int discount(IsMemberShip isMemberShip, int price);

}
