package shoppingMall.gupang.service.coupon;

import shoppingMall.gupang.controller.coupon.CouponDto;
import shoppingMall.gupang.domain.coupon.Coupon;

public interface CouponService {

    void registerCoupon(Long memberId, CouponDto couponDto);

}
