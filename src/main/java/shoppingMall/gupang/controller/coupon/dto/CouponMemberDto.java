package shoppingMall.gupang.controller.coupon.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponMemberDto {

    private Long couponId;
    private String couponName;
    private String itemName;
    private String couponType;
    private int discountAmount;
    private LocalDateTime expireDate;

    public CouponMemberDto(Long couponId, String itemName, String couponType, int discountAmount,
                           LocalDateTime expireDate, String couponName) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.itemName = itemName;
        this.couponType = couponType;
        this.discountAmount = discountAmount;
        this.expireDate = expireDate;
    }
}
