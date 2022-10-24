package shoppingMall.gupang.controller.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.controller.order.dto.OrderDto;
import shoppingMall.gupang.controller.order.dto.OrderReturnDto;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.service.order.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
@Tag(name = "order", description = "주문 api")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "add order", description = "주문 추가하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 회원이 없습니다.")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @PostMapping("/add")
    public String order(@RequestBody OrderDto orderDto) {
        Address address = new Address(orderDto.getCity(), orderDto.getStreet(), orderDto.getZipcode());
        orderService.order(address, orderDto);
        return "ok";
    }

    @Operation(summary = "order with coupon", description = "쿠폰과 함께 주문하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 회원이 없습니다."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 없습니다.")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = OrderCouponDto.class)))
    @PostMapping("/coupon")
    public String orderWithCoupon(@RequestBody OrderCouponDto orderCouponDto) {
        orderService.orderWithCoupon(orderCouponDto);
        return "ok";
    }

    @Operation(summary = "get order", description = "주문 불러오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = OrderReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 회원이 없습니다."),
    })
    @Parameter(name = "memberId", description = "회원 아이디")
    @PostMapping
    public List<OrderReturnDto> getOrder(@RequestParam(value = "memberId") Long memberId) {
        return orderService.getOrderByMember(memberId).stream()
                .map(o -> new OrderReturnDto(o.getId(), o.getOrderItems()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "cancel order", description = "주문 취소하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 주문이 없습니다."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 이미 취소된 주문입니다.")
    })
    @Parameter(name = "orderId", description = "주문 아이디")
    @PatchMapping
    public String cancelOrder(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
        return "ok";
    }

}
