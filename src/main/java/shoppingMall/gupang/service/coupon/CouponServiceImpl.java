package shoppingMall.gupang.service.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.web.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.web.controller.coupon.dto.CouponMemberDto;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.coupon.Coupon;
import shoppingMall.gupang.domain.coupon.DeliveryCoupon;
import shoppingMall.gupang.domain.coupon.FixCoupon;
import shoppingMall.gupang.domain.coupon.PercentCoupon;
import shoppingMall.gupang.exception.coupon.CouponExpireException;
import shoppingMall.gupang.exception.coupon.NoCouponTypeException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.repository.coupon.CouponRepository;
import shoppingMall.gupang.repository.coupon.DeliveryCouponRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    쿠폰 생성시 쿠폰 허용 기간, 멤버, 상품, 종류등을 모두 확인하고 저장하기 때문에
    멤버가 갖고 있는 쿠폰을 유효기간 제외하고 다른 부분을 확인할 필요는 없다.
 */

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final DeliveryCouponRepository deliveryCouponRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Override
    public void registerCoupon(CouponDto couponDto) {
        saveCoupon(couponDto);
    }

    private void saveCoupon(CouponDto couponDto) {
        log.info(String.valueOf(couponDto.getExpireDate()));
        if (couponDto.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new CouponExpireException("쿠폰 허용 기한이 현재보다 과거입니다.");
        }

        Optional<Member> optionalMember = memberRepository.findById(couponDto.getMemberId());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        Optional<Item> optionalItem = itemRepository.findById(couponDto.getItemId());
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }

        Coupon coupon;
        if (couponDto.getCouponType().equals("Fix")) {
            coupon = new FixCoupon(member, item, couponDto.getExpireDate(),"Fix",
                    couponDto.getDiscountAmount(), couponDto.getCouponName());
        } else if (couponDto.getCouponType().equals("Percent")) {
            coupon = new PercentCoupon(member, item, couponDto.getExpireDate(), "Percent",
                    couponDto.getDiscountAmount(), couponDto.getCouponName());
        } else {
            throw new NoCouponTypeException("해당하는 쿠폰 타입이 없습니다.");
        }

        couponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponMemberDto> getUnusedCoupons(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        List<CouponMemberDto> couponMemberDtos = new ArrayList<>();
        List<Coupon> coupons = couponRepository.findCouponByMemberWithItem(memberId);
        return coupons.stream().map(c -> new CouponMemberDto(c.getId(), c.getItem().getName(), c.getCouponType(),
                c.getDiscountAmount(), c.getExpireDate(), c.getUsed(), c.getCouponName()))
                .collect(Collectors.toList());
    }
}
