package shoppingMall.gupang.web.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.controller.member.MemberDto;
import shoppingMall.gupang.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.service.member.MemberService;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PostMapping("/checkId")
    public String checkExistEmail(@RequestBody EmailDto emailDto) {
        Optional<Member> optionalMember = memberRepository.findOptionalByEmail(emailDto.getEmail());
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

    @PostMapping("/signup")
    public String signup(@RequestBody MemberDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findOptionalByEmail(memberDto.getEmail());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            memberService.registerMember(memberDto);
            return "signup";
        }

        throw new AlreadyMemberExistException("이미 아이디가 존재합니다.");
    }

//    @PostMapping("/review")
//    public Result getMemberReviews(Long memberId) {
//        List<ReviewReturnDto> collect = memberService.getMemberReviews(memberId).stream()
//                .map(r -> new ReviewReturnDto(r.getId(), r.getTitle(), r.getContents()))
//                .collect(Collectors.toList());
//
//        return new Result(collect);
//    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
