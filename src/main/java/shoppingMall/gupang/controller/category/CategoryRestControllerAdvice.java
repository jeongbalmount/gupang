package shoppingMall.gupang.controller.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.category.NoCategoryException;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;

@RestControllerAdvice
public class CategoryRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noCategoryException(NoCategoryException e) {
        return new ErrorResult("No category", e.getMessage());
    }
}
