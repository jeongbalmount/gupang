package shoppingMall.gupang.web.controller.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.web.exception.AlreadyEmailExistException;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;

@RestControllerAdvice(assignableTypes = MemberController.class)
public class MemberControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult alreadyEmailExistException(AlreadyEmailExistException e) {
        return new ErrorResult("AlreadyEmailExistException", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyMemberExistException.class)
    public ErrorResult alreadyMemberException(AlreadyMemberExistException e) {
        return new ErrorResult("AlreadyMemberExist", e.getMessage());
    }

}
