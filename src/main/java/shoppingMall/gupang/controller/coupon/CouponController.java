package shoppingMall.gupang.controller.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.controller.coupon.dto.CouponMemberDto;
import shoppingMall.gupang.service.coupon.CouponService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public List<CouponMemberDto> getMemberCoupons(Long memberId) {
        return couponService.getUnusedCoupons(memberId);
    }

    @PostMapping("/add")
    public String addCoupon(@Valid @RequestBody CouponDto couponDto) {
        couponService.registerCoupon(couponDto);
        return "ok";
    }

}
