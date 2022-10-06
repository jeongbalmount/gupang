package shoppingMall.gupang.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.item.NotEnoughStockException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.order.AlreadyCanceledOrderException;
import shoppingMall.gupang.exception.order.NoOrderException;

@RestControllerAdvice(assignableTypes = {OrderController.class})
public class OrderRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noMemberException(NoMemberException e) {
        return new ErrorResult("No member", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noItemException(NoItemException e) {
        return new ErrorResult("No item", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noOrderException(NoOrderException e) {
        return new ErrorResult("No order", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult notEnoughStockException(NotEnoughStockException e) {
        return new ErrorResult("Not enough item stock", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult alreadyCanceledOrderException(AlreadyCanceledOrderException e) {
        return new ErrorResult("Already canceled order", e.getMessage());
    }

}
