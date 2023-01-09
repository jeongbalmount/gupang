package shoppingMall.gupang.elasticsearch.coupon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.coupon.AlreadyCouponUsedException;
import shoppingMall.gupang.exception.coupon.CouponExpiredException;

import java.time.LocalDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "fixcoupon")
public class FixCoupon {

    @Id
    private String id;

    private Long memberid;

    private Long itemid;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime expiredate;

    private Boolean used = false;

    private int discountamount;

    public FixCoupon(Long member_id, Long item_id, LocalDateTime expireDate, int discountAmount) {
        this.memberid = member_id;
        this.itemid = item_id;
        this.expiredate = expireDate;
        this.discountamount = discountAmount;
    }

    public void useCoupon() {
        checkCouponValid();
        this.used = true;
    }

    public int getCouponAppliedPrice(int price){
        int discountedPrice = price - this.getDiscountamount();
        return Math.max(discountedPrice, 0);
    };

    public void checkCouponValid() {
        checkExpireDate();
        checkUsed();
    }

    private void checkUsed() {
        if (this.used) {
            throw new AlreadyCouponUsedException("이미 사용된 쿠폰입니다.");
        }
    }

    private void checkExpireDate(){
        if (expiredate.isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException("이미 기한이 지난 쿠폰입니다.");
        }
    };

}
