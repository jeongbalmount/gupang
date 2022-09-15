package shoppingMall.gupang.service.member;

import shoppingMall.gupang.controller.member.MemberDto;
import shoppingMall.gupang.domain.Member;

public interface MemberService {

    void registerMember(MemberDto memberDto);

    void changeMemberShipStatus(Long memberId);

    void memberOutService(Long memberId);

    Member getMember(Long memberId);

//    void addCoupon(Long memberId, Coupon coupon);

}
