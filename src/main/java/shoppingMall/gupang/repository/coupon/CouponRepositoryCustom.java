package shoppingMall.gupang.repository.coupon;

import shoppingMall.gupang.domain.coupon.Coupon;

import java.util.List;

public interface CouponRepositoryCustom {

    List<Coupon> findCouponByMemberWithItem(Long memberId);
}
