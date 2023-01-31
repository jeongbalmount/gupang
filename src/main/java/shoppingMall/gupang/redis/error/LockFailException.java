package shoppingMall.gupang.redis.error;

public class LockFailException extends RuntimeException{

    public LockFailException(String message) {
        super(message);
    }
}
