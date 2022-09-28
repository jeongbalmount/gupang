package shoppingMall.gupang.web.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;
import shoppingMall.gupang.web.login.LoginController;
import shoppingMall.gupang.web.member.MemberController;

@RestControllerAdvice(assignableTypes = {MemberController.class, LoginController.class})
public class LoginControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult alreadyMemberException(AlreadyMemberExistException e) {
        return new ErrorResult("AlreadyMemberExist", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        return new ErrorResult("IllegalArgument Exception", e.getMessage());
    }


}
