package shoppingMall.gupang.controller.coupon.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CouponDto {

    @NotEmpty
    private Long applyItemId;

    @NotEmpty
    private LocalDateTime expireDate;

    @NotEmpty
    private int discountAmount;

    @NotEmpty
    private boolean isFixCoupon;

    public CouponDto(Long applyItemId, LocalDateTime expireDate, boolean isFixCoupon, int discountAmount) {
        this.applyItemId = applyItemId;
        this.expireDate = expireDate;
        this.isFixCoupon = isFixCoupon;
        this.discountAmount = discountAmount;
    }
}
