package shoppingMall.gupang.exception.order;

public class AlreadyCanceledOrderException extends RuntimeException{

    public AlreadyCanceledOrderException(String message) {
        super(message);
    }
}
