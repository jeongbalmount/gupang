package shoppingMall.gupang.service.coupon;

import shoppingMall.gupang.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.controller.coupon.dto.CouponMemberDto;
import shoppingMall.gupang.domain.coupon.Coupon;

import java.util.List;

public interface CouponService {

    void registerCoupon(Long memberId, Long itemId, CouponDto couponDto);

    List<CouponMemberDto> getUnusedCoupons(Long memberId);

}
