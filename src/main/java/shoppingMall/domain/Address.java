package shoppingMall.domain;

// 값 타입
public class Address {

    private String city;

    private String street;

    private String zipcode;

    protected Address() {}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
