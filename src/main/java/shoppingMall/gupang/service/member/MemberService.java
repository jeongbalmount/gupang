package shoppingMall.gupang.service.member;

import shoppingMall.gupang.controller.member.MemberDto;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.coupon.Coupon;

public interface MemberService {

    Long registerMember(MemberDto memberDto);

    void changeMemberShipStatus(Long memberId);

    void memberOutService(Long memberId);

    Member getMember(Long memberId);
}
