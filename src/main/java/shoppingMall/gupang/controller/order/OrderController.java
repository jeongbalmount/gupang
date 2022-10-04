package shoppingMall.gupang.controller.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.controller.order.dto.OrderDto;
import shoppingMall.gupang.controller.order.dto.OrderReturnDto;
import shoppingMall.gupang.service.order.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public String order(@RequestBody OrderDto orderDto) {
        orderService.order(orderDto);
        return "ok";
    }

    @PostMapping("/coupon")
    public String orderWithCoupon(@RequestBody OrderCouponDto orderCouponDto) {
        orderService.orderWithCoupon(orderCouponDto);
        return "ok";
    }

//    @PostMapping("/")
//    public OrderReturnDto getOrder(@RequestParam Long memberId) {
//        orderService.getOrderByMember(memberId).stream()
//                .map(o -> )
//    }



}
