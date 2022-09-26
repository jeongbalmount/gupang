package shoppingMall.gupang.web.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;
import shoppingMall.gupang.web.login.LoginController;
import shoppingMall.gupang.web.member.ApiExceptionController;
import shoppingMall.gupang.web.member.MemberController;

@RestControllerAdvice(assignableTypes = {MemberController.class, LoginController.class, ApiExceptionController.class})
public class LoginControllerAdvice {

    @ExceptionHandler
    public ResponseEntity alreadyMemberException(AlreadyMemberExistException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalExHandler(IllegalArgumentException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
