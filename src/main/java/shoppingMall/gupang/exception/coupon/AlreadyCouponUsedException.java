package shoppingMall.gupang.exception.coupon;

public class AlreadyCouponUsedException extends RuntimeException {
    public AlreadyCouponUsedException(String message) {
        super(message);
    }
}
