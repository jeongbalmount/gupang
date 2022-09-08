package shoppingMall.gupang.domain;

import java.time.LocalDateTime;


public class Order {

    private Long id;

    private LocalDateTime orderDate;

    private OrderItem orderItem;

    private int charge;

    private DeliveryStatus deliveryStatus;

}
