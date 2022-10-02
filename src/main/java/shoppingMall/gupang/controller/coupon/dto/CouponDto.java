package shoppingMall.gupang.controller.coupon.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CouponDto {

    private Long memberId;

    private Long itemId;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expireDate;

    private int discountAmount;

    private String couponType;

}
