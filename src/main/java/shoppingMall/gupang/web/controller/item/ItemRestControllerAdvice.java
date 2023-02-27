package shoppingMall.gupang.web.controller.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.category.NoCategoryException;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.exception.seller.NoSellerException;

@RestControllerAdvice(assignableTypes = {ItemController.class})
public class ItemRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noSellerException(NoSellerException e) {
        return new ErrorResult("No seller", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noCategoryException(NoCategoryException e) {
        return new ErrorResult("No category", e.getMessage());
    }

}
