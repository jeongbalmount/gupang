package shoppingMall.gupang.exception.cart;

public class LackOfCountException extends RuntimeException{

    public LackOfCountException(String message) {
        super(message);
    }
}
