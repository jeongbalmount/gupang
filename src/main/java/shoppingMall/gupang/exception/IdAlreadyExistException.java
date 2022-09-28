package shoppingMall.gupang.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IdAlreadyExistException extends RuntimeException{

    public IdAlreadyExistException(String message) {
        super(message);
    }
}
