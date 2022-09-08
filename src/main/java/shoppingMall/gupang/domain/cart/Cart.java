package shoppingMall.gupang.domain.cart;


import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Cart {

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToMany(mappedBy = "cart")
    private List<Item> items = new ArrayList<>();
    // 상품 개수는 다 일대다로 다 가져오고 소분화 한다.

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
