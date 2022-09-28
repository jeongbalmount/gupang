package shoppingMall.gupang.globalcontrolleradvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.web.exception.AuthenticationsException;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationsException.class)
    public ErrorResult authenticationException(AuthenticationsException e) {
        return new ErrorResult("AUTHENTICATION ERROR", e.getMessage());
    }
}
