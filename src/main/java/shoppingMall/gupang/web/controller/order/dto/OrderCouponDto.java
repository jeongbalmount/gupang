package shoppingMall.gupang.web.controller.order.dto;

import lombok.*;
import shoppingMall.gupang.domain.Address;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderCouponDto {

    @NotNull
    private Long memberId;

    // 주문시 주소는 회원 주소와 다를 수 있다.
    @NotNull
    private Address address;

    @NotNull
    private List<OrderItemDto> orderItemDtos;

    private List<Long> couponIds;

    private List<Long> deliveryCouponIds;
}
