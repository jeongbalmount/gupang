package shoppingMall.gupang.service.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.coupon.CouponDto;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.exception.NoItemException;
import shoppingMall.gupang.exception.NoMemberException;
import shoppingMall.gupang.repository.coupon.CouponRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Override
    public void registerCoupon(Long memberId, Long itemId, CouponDto couponDto) {
        saveCoupon(memberId, itemId, couponDto);
    }

    private void saveCoupon(Long memberId, Long itemId, CouponDto couponDto) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }

        Coupon coupon;
        if (couponDto.isFixCoupon()) {
            coupon = new FixCoupon(member, item, couponDto.getExpireDate(), couponDto.getDiscountAmount());
        } else {
            coupon = new PercentCoupon(member, item, couponDto.getExpireDate(), couponDto.getDiscountAmount());
        }

        couponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coupon> getUnusedCoupons(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        return couponRepository.findByMember(member);

    }
}
