package shoppingMall.gupang.web.controller.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.cart.LackOfCountException;
import shoppingMall.gupang.exception.cart.NoCartItemException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;

@RestControllerAdvice(assignableTypes = {CartController.class})
public class CartRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noMemberException(NoMemberException e){
        return new ErrorResult("NoMember", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noItemException(NoItemException e){
        return new ErrorResult("NoItem", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noCartItemException(NoCartItemException e){
        return new ErrorResult("NoCartItem", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult lackOfCountException(LackOfCountException e){
        return new ErrorResult("LackOfCount", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult cartItemNotMatchWithMemberException(LackOfCountException e){
        return new ErrorResult("카트 상품의 회원에 해당하지 않습니다.", e.getMessage());
    }

}
