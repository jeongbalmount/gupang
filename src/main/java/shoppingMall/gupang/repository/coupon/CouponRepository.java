package shoppingMall.gupang.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.coupon.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
