package shoppingMall.gupang.controller.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.coupon.CouponExpireException;
import shoppingMall.gupang.exception.coupon.NoCouponTypeException;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;

@RestControllerAdvice(assignableTypes = {CouponController.class})
public class CouponRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noCouponTypeException(NoCouponTypeException e) {
        return new ErrorResult("NoCoupon type", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult couponExpireException(CouponExpireException e) {
        return new ErrorResult("NoCoupon type", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noMemberException(NoMemberException e) {
        return new ErrorResult("No Member", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noItemException(NoItemException e) {
        return new ErrorResult("No item", e.getMessage());
    }

}
