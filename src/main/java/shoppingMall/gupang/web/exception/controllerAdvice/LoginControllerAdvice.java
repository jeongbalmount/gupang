package shoppingMall.gupang.web.exception.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;
import shoppingMall.gupang.web.exception.LoginAccessErrorException;
import shoppingMall.gupang.web.login.LoginController;
import shoppingMall.gupang.web.member.MemberController;

@RestControllerAdvice(assignableTypes = {MemberController.class, LoginController.class})
public class LoginControllerAdvice {

    @ExceptionHandler(AlreadyMemberExistException.class)
    public ResponseEntity alreadyMemberException(Exception e) {
        return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginAccessErrorException.class)
    public ResponseEntity loginAccessErrorException(Exception e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
