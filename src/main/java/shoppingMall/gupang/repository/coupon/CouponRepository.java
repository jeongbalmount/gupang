package shoppingMall.gupang.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.coupon.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByMember(Member member);

}
