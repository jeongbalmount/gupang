package shoppingMall.gupang.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
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
    }

    public static OrderItem createOrderItem(Item item, int itemPrice, int count, int couponDiscountAmount) {
        OrderItem orderItem = new OrderItem(item, itemPrice, count, couponDiscountAmount);
        item.removeStock(count);

        return orderItem;
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    private int couponDiscountAmount;

    private int itemCount;

    private int itemPrice;


    public int getTotalPrice() {
        return itemCount * itemPrice;
    }

    public void cancel() {
        getItem().addStock(itemCount);
    }
}
