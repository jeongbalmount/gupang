package shoppingMall.gupang.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private OrderItem(Item item, int itemPrice, int itemCount, int couponDiscountAmount) {
        this.item = item;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.couponDiscountAmount = couponDiscountAmount;
        /*
        - coupon으로 할인되는 상품은 1개여야 한다. 그렇기 때문에 1개의 상품만 couponDiscountAmount를 적용하고
        - 나머지는 정상가격으로 등록해야 한다.
        - 그렇기 때문에 totalPrice를 따로 만들어 쿠폰이 있는경우 상품 1개에 대해 쿠폰을 적용해야 한다.
     */
        if (itemPrice >= couponDiscountAmount) {
            this.totalPrice = (itemPrice - couponDiscountAmount) + (itemPrice * (itemCount - 1));
        } else {
            this.totalPrice = itemPrice * itemCount;
        }
    }

    public static OrderItem createOrderItem(Item item, int itemPrice, int count, int couponDiscountAmount) {
        return new OrderItem(item, itemPrice, count, couponDiscountAmount);
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    private int itemCount;

    private int itemPrice;

    private int couponDiscountAmount;

    private int totalPrice;
}
