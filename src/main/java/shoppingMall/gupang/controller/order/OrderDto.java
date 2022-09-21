package shoppingMall.gupang.controller.order;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderDto {

    @NotEmpty
    private Long memberId;
    @NotEmpty
    private Long itemId;
    @NotEmpty
    private int itemCount;
    @NotEmpty
    private String city;
    @NotEmpty
    private String street;
    @NotEmpty
    private String zipcode;

    public OrderDto(Long itemId, int itemCount, String city, String street, String zipcode) {
        this.itemId = itemId;
        this.itemCount = itemCount;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
