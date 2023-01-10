package shoppingMall.gupang.domain.coupon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.coupon.AlreadyCouponUsedException;
import shoppingMall.gupang.exception.coupon.CouponExpiredException;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DeliveryCoupon {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime expireDate;

    private Boolean used = false;

    public DeliveryCoupon(Member member, LocalDateTime expireDate) {
        this.member = member;
        this.expireDate = expireDate;
    }

    public void useCoupon() {
        checkCouponValid();
        this.used = true;
    }

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
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException("이미 기한이 지난 쿠폰입니다.");
        }
    };
}
