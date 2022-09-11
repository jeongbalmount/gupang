package shoppingMall.gupang.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int itemCount;

    private int itemPrice;

    public CartItem(Member member, Item item, int itemCount, int itemPrice) {
        this.member = member;
        this.item = item;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
    }




    public void updateItemCount(int count) {
        this.itemCount = count;
    }

    public void setCartItemMember(Member member) {
        this.member = member;
    }

    public int getItemTotalPrice() {
        return itemPrice * itemPrice;
    }


}
