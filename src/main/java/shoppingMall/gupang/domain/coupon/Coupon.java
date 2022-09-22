package shoppingMall.gupang.domain.coupon;

import lombok.Getter;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.AlreadyCouponUsedException;
import shoppingMall.gupang.exception.CouponExpiredException;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity @Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Coupon {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    private LocalDateTime expireDate;
    private Boolean used;

    public Coupon(Member member, Item item, LocalDateTime expireDate) {
        this.member = member;
        this.item = item;
        this.expireDate = expireDate;
        this.used = false;
    }
    public Coupon() {

    }

    public void useCoupon() {
        checkCouponValid();
        this.used = true;
    }

    public abstract int getCouponAppliedPrice(int price);

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
