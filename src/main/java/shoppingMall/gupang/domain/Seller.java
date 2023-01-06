package shoppingMall.gupang.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class Seller {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String phoneNumber;

    private String managerName;

    public Seller(String phoneNumber, String managerName) {
        this.phoneNumber = phoneNumber;
        this.managerName = managerName;
    }

    public void addSellingItemStock(Item item) {
        // 들어오기 전에 item에 seller 객체를 참조하도록 설정하고 들어온다.
        item.registerSeller(this);
    }

    public void updateManagerName(String newName) {
        this.managerName = newName;
    }

}
