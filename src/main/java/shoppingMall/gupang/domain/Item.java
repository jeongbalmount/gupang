package shoppingMall.gupang.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shoppingMall.gupang.exception.item.NotEnoughStockException;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Slf4j
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int itemPrice;

    private int itemQuantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Item(String name, int itemPrice, int itemQuantity, Seller seller, Category category) {
        this.name = name;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.seller = seller;
        this.category = category;
    }

    public void addStock(int itemCount) {
        this.itemQuantity += itemCount;
    }

    public void registerSeller(Seller seller) {
        this.seller = seller;
    }

    public void removeStock(int itemCount) {
        int restStock = this.itemQuantity - itemCount;
        if (restStock < 0) {
            throw new NotEnoughStockException("상품 재고 수량이 부족합니다.");
        }
        this.itemQuantity = restStock;
    }
}