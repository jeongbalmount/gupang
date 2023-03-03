package shoppingMall.gupang.web.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.service.member.MemberService;
import shoppingMall.gupang.web.controller.member.MemberDto;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping
    public String checkExistEmail(@RequestBody EmailDto emailDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(emailDto.getEmail());
        Member member = optionalMember.orElse(null);
        if (member == null){
            return "ok";
        } else {
            return "alreadyExist";
        }
    }

    @Data
    private static class EmailDto {
        private String email;
    }

    @PostMapping
    public String signup(@RequestBody MemberDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDto.getEmail());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            memberService.registerMember(memberDto);
            return "signup";
        }

        throw new AlreadyMemberExistException("이미 아이디가 존재합니다.");
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
