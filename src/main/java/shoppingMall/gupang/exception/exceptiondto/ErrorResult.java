package shoppingMall.gupang.exception.exceptiondto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {

    private String errorCode;
    private String message;

}
