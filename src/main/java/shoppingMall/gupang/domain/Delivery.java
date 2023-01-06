package shoppingMall.gupang.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingMall.gupang.domain.enums.DeliveryStatus;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    private int deliveryFee;

    private DeliveryStatus deliveryStatus;

    public Delivery(Address address, int deliveryFee, DeliveryStatus deliveryStatus) {
        this.address = address;
        this.deliveryFee = deliveryFee;
        this.deliveryStatus = deliveryStatus;
    }

    public void goDelivery() {
        // 나중에 firebase와 연동해서 실제 배달이 나가는 식으로 연동해보기
        if (deliveryStatus == DeliveryStatus.READY) {
            this.deliveryStatus = DeliveryStatus.DELIVERED;
        } else {
            throw new IllegalStateException("이미 배송된 건 입니다.");
        }
    }

}
