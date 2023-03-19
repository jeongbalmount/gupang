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
    }

    public static OrderItem createOrderItem(Item item, int itemPrice, int count, int couponDiscountAmount) {
        return new OrderItem(item, itemPrice, count, couponDiscountAmount);
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    private int itemCount;

    private int itemPrice;


    public int getTotalPrice() {
        return itemCount * itemPrice;
    }

}
