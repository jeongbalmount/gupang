package shoppingMall.gupang.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.member.MemberDto;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.repository.member.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public Long registerMember(MemberDto memberDto) {
        Address address = new Address(memberDto.getCity(), memberDto.getStreet(), memberDto.getZipcode());

        Member member = new Member(memberDto.getEmail(), memberDto.getPassword(), memberDto.getName(),
                memberDto.getPhoneNumber(), address, IsMemberShip.NOMEMBERSHIP);

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Override
    public void changeMemberShipStatus(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            return;
        }
        member.registerMembership();
    }

    @Override
    public void memberOutService(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) return;
        memberRepository.delete(member);
    }

    @Override
    public Member getMember(Long memberId) {
        Optional<Member> OptionalMember = memberRepository.findById(memberId);
        Member member = OptionalMember.orElse(null);
        if (member == null) {
            return null;
        }
        return member;
    }
}
