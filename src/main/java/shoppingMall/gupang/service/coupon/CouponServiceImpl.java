package shoppingMall.gupang.service.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.coupon.CouponDto;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.exception.NoMemberException;
import shoppingMall.gupang.repository.coupon.CouponRepository;
import shoppingMall.gupang.repository.member.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    @Override
    public void registerCoupon(Long memberId, CouponDto couponDto) {
        saveCoupon(memberId, couponDto);
    }

    private void saveCoupon(Long memberId, CouponDto couponDto) {
        Coupon coupon;
        if (couponDto.isFixCoupon()) {
            coupon = new FixCoupon(couponDto.getApplyItemId(), couponDto.getExpireDate(),
                    couponDto.getDiscountAmount());
        } else {
            coupon = new PercentCoupon(couponDto.getApplyItemId(), couponDto.getExpireDate(),
                    couponDto.getDiscountAmount());
        }

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }
        coupon.registerCouponUser(member);
        couponRepository.save(coupon);
    }
}
