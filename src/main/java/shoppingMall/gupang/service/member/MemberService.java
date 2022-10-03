package shoppingMall.gupang.service.member;

import shoppingMall.gupang.controller.member.MemberDto;
import shoppingMall.gupang.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Review;

import java.util.List;

public interface MemberService {

    Long registerMember(MemberDto memberDto);

    void changeMemberShipStatus(Long memberId);

    void memberOutService(Long memberId);

    Member getMember(Long memberId);

    List<Review> getMemberReviews(Long memberId);
}
