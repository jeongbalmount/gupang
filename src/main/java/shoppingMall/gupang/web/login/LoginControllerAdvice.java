package shoppingMall.gupang.web.login;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.web.exception.AlreadyEmailExistException;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;
import shoppingMall.gupang.web.exception.LoginFailedException;
import shoppingMall.gupang.web.controller.member.MemberController;

@RestControllerAdvice(assignableTypes = LoginController.class)
public class LoginControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        return new ErrorResult("IllegalArgumentException", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailedException.class)
    public ErrorResult loginFailedException(LoginFailedException e) {
        return new ErrorResult("LoginFailed", e.getMessage());
    }

}
