package shoppingMall.gupang.domain.coupon;

import shoppingMall.gupang.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Coupon {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    Long applyItemId;
    LocalDateTime expireDate;

    public Coupon(Long applyItemId, LocalDateTime expireDate) {
        this.applyItemId = applyItemId;
        this.expireDate = expireDate;
    }

    public Coupon() {

    }

    public abstract int getCouponAppliedPrice(int price);

    public boolean isExpiredCoupon(Coupon coupon){
        if (expireDate.isBefore(LocalDateTime.now())) {
            return false;
        } else {
            return true;
        }
    };

    public void registerCouponUser(Member member) {
        this.member = member;
    }

}
