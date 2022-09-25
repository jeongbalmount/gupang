package shoppingMall.gupang.web.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.exception.LoginAccessErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            // https://velog.io/@monkeydugi/Spring-Interceptor%EC%97%90%EC%84%9C-%EC%98%88%EC%99%B8%EB%A5%BC-%EC%9D%91%EB%8B%B5-%ED%95%B4%EC%A3%BC%EB%8A%94-%EB%B0%A9%EB%B2%95
            throw new LoginAccessErrorException("로그인이 안된 사용자 입니다.");
        }

        return true;
    }
}
