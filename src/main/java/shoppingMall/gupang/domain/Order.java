package shoppingMall.gupang.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shoppingMall.gupang.domain.enums.DeliveryStatus;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.order.AlreadyDeliveredException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @Slf4j
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders") // sql에서 order라는 이름의 테이블은 사용할 수 없다!
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // , cascade = CascadeType.ALL, orphanRemoval = true
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(STRING)
    private IsMemberShip memberShipOrder;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private Order(LocalDateTime orderDate, Member member, Delivery delivery, IsMemberShip memberShipOrder,
                  OrderStatus orderStatus, int totalPrice) {
        this.orderDate = orderDate;
        this.member = member;
        this.delivery = delivery;
        this.memberShipOrder = memberShipOrder;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }

    // 생성 메서드
    public static Order createOrder(LocalDateTime orderDate, Member member,
                                    Delivery delivery, IsMemberShip memberShipOrder,
                                    OrderStatus orderStatus, List<OrderItem> orderItems, int totalPrice) {
        Order order = new Order(orderDate, member, delivery, memberShipOrder, orderStatus, totalPrice);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    public void cancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw new AlreadyDeliveredException("이미 배송 처리 되었습니다.");
        }

        this.orderStatus = OrderStatus.CANCEL;
    }

    // 연관관계 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.registerOrder(this);
    }

}