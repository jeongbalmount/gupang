package shoppingMall.gupang.web.controller.member;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.service.member.MemberService;
import shoppingMall.gupang.web.controller.member.dto.MemberDto;
import shoppingMall.gupang.web.exception.AlreadyEmailExistException;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/{memberEmail}")
    public String checkExistEmail(@PathVariable String memberEmail) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null){
            return "ok";
        } else {
            throw new AlreadyEmailExistException("이미 존재하는 이메일이 있습니다.");
        }
    }

    @PostMapping
    public String signup(@RequestBody MemberDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDto.getEmail());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            memberService.registerMember(memberDto);
            return "ok";
        }

        throw new AlreadyMemberExistException("이미 아이디가 존재합니다.");
    }

}
