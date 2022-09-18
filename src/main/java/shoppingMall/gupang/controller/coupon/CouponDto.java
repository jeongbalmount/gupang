package shoppingMall.gupang.controller.coupon;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponDto {

    // Long applyItemId, LocalDateTime expireDate
    private Long applyItemId;
    private LocalDateTime expireDate;

    private int discountAmount;
    private boolean isFixCoupon;

    public CouponDto(Long applyItemId, LocalDateTime expireDate, boolean isFixCoupon, int discountAmount) {
        this.applyItemId = applyItemId;
        this.expireDate = expireDate;
        this.isFixCoupon = isFixCoupon;
        this.discountAmount = discountAmount;
    }
}
