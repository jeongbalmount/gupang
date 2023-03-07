package shoppingMall.gupang.web.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.exception.AuthenticationsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        log.info(String.valueOf(session.getAttribute(SessionConst.LOGIN_MEMBER)));
        if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AuthenticationsException("권한이 없습니다. 로그인 해주세요");
        }

        return true;
    }
}
