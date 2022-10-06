package shoppingMall.gupang.controller.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    public String order(@RequestBody OrderDto orderDto) {
        Address address = new Address(orderDto.getCity(), orderDto.getStreet(), orderDto.getZipcode());
        orderService.order(address, orderDto);
        return "ok";
    }

    @PostMapping("/coupon")
    public String orderWithCoupon(@RequestBody OrderCouponDto orderCouponDto) {
        orderService.orderWithCoupon(orderCouponDto);
        return "ok";
    }

    @PostMapping
    public List<OrderReturnDto> getOrder(@RequestParam(value = "memberId") Long memberId) {
        return orderService.getOrderByMember(memberId).stream()
                .map(o -> new OrderReturnDto(o.getId(), o.getOrderItems()))
                .collect(Collectors.toList());
    }

    @PatchMapping
    public String cancelOrder(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
        return "ok";
    }

}
