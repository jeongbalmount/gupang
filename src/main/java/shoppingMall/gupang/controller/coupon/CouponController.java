package shoppingMall.gupang.controller.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.controller.coupon.dto.CouponMemberDto;
import shoppingMall.gupang.service.coupon.CouponService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public List<CouponMemberDto> getMemberCoupons(Long memberId) {
        return couponService.getUnusedCoupons(memberId);
    }

}
