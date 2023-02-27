package shoppingMall.gupang.service.coupon;

import shoppingMall.gupang.web.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.web.controller.coupon.dto.CouponMemberDto;

import java.util.List;

public interface CouponService {

    void registerCoupon(CouponDto couponDto);

    List<CouponMemberDto> getUnusedCoupons(Long memberId);

}
