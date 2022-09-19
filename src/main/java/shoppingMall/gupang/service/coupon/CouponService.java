package shoppingMall.gupang.service.coupon;

import shoppingMall.gupang.controller.coupon.CouponDto;
import shoppingMall.gupang.domain.coupon.Coupon;

import java.util.List;

public interface CouponService {

    void registerCoupon(Long memberId, CouponDto couponDto);

    List<Coupon> getUnusedCoupons(Long memberId);

}
