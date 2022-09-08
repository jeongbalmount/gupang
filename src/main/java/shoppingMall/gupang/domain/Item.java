package shoppingMall.gupang.domain;

public class Item {

    private Long id;

    private Category category;

    // 할인률 방식은 인터페이스로 따로 만들기(퍼센트 or 할인 가격 고정)

    private Seller seller;

    private int Quantity;
}
