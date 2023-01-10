package shoppingMall.gupang.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.coupon.DeliveryCoupon;

import java.util.List;

public interface DeliveryCouponRepository extends JpaRepository<DeliveryCoupon, Long> {

    List<DeliveryCoupon> findByMember(Member member);

}
