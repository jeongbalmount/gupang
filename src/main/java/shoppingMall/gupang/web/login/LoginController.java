package shoppingMall.gupang.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.exception.LoginFailedException;
import shoppingMall.gupang.web.login.dto.LoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto loginDto, HttpSession session) {

        Member loginMember = loginService.Login(loginDto.getEmail(), loginDto.getPassword());
        if (loginMember == null) {
            throw new LoginFailedException("로그인에 실패하였습니다.");
        }

        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getEmail());
        return "ok";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "ok";
    }

    @GetMapping("/")
    public String healthCheck(HttpServletRequest request) {
        return "ok";
    }

}
