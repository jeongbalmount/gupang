package shoppingMall.gupang.service.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.coupon.dto.CouponDto;
import shoppingMall.gupang.controller.coupon.dto.CouponMemberDto;
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

import java.util.ArrayList;
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
    public List<CouponMemberDto> getUnusedCoupons(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        List<CouponMemberDto> couponMemberDtos = new ArrayList<>();
        couponRepository.findCouponByMemberWithItem(memberId);
        return transformToDto(couponMemberDtos, couponRepository.findCouponByMemberWithItem(memberId));
    }

    private List<CouponMemberDto> transformToDto(List<CouponMemberDto> dtoList, List<Coupon> coupons) {
        for (Coupon c : coupons) {
            if (!c.getUsed()) {
                CouponMemberDto dto = new CouponMemberDto(c.getId(), c.getItem().getName(),
                        c.getCouponType(), c.getDiscountAmount(), c.getExpireDate());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
}
