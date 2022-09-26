package shoppingMall.gupang.web.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.controller.member.MemberDto;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.service.member.MemberService;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PostMapping("/checkId")
    public String checkExistEmail(@RequestBody String memberEmail) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null){
            return "ok";
        } else {
            return "alreadyExist";
        }
    }

    @PostMapping("/signup")
    public String Signup(@RequestBody MemberDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDto.getEmail());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            memberService.registerMember(memberDto);
            return "signup";
        }

        throw new AlreadyMemberExistException("이미 아이디가 존재합니다.");
    }

}
