package shoppingMall.gupang.controller.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.controller.coupon.dto.CouponMemberDto;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.service.coupon.CouponService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "get member coupons", description = "회원이 가지고 있는 쿠폰 목록 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CouponMemberDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
    })
    @Parameter(name = "memberId", description = "카테고리 id")
    @GetMapping("/{memberId}")
    public List<CouponMemberDto> getMemberCoupons(@PathVariable Long memberId) {
        return couponService.getUnusedCoupons(memberId);
    }

    @Operation(summary = "add new coupon", description = "새로운 쿠폰 등록하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 멤버가 존재하지 않음"),
    })
    @Parameter(content = @Content(schema = @Schema(implementation = CouponDto.class)))
    @PostMapping("/add")
    public String addCoupon(@Valid @RequestBody CouponDto couponDto) {
        couponService.registerCoupon(couponDto);
        return "ok";
    }

}
