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

    public OrderItem(Order order, Item item, int itemCount, int itemPrice) {
        this.order = order;
        this.item = item;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
    }

    public void registerOrder(Order order) {
        this.order = order;
    }


    private int itemCount;

    private int itemPrice;


    public int getTotalPrice() {
        return itemCount * itemPrice;
    }

    public void cancel() {
        getItem().addStock(itemCount);
    }
}
