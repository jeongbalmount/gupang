package shoppingMall.gupang.web.controller.review;

import co.elastic.clients.elasticsearch._types.query_dsl.Like;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingMall.gupang.exception.exceptiondto.ErrorResult;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.review.LikeLimitException;
import shoppingMall.gupang.exception.review.NoEditedContentException;
import shoppingMall.gupang.exception.review.NoMatchEmailException;
import shoppingMall.gupang.exception.review.NoReviewException;

@RestControllerAdvice
public class ReviewRestControllerAdvice {

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
    public ErrorResult noReviewException(NoReviewException e) {
        return new ErrorResult("No review", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noEditedContentException(NoEditedContentException e) {
        return new ErrorResult("No edited contents", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult likeLimitException(LikeLimitException e) {
        return new ErrorResult("Like count exceeded", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noMatchEmailException(NoMatchEmailException e) {
        return new ErrorResult("Can't update review", e.getMessage());
    }
}
