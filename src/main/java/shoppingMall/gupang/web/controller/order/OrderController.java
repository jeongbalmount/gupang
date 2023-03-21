package shoppingMall.gupang.web.controller.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.order.dto.OrderCouponDto;
import shoppingMall.gupang.web.controller.order.dto.OrderReturnDto;
import shoppingMall.gupang.service.order.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
@Tag(name = "order", description = "주문 api")
public class OrderController {

    private final OrderService orderService;

//    @Operation(summary = "add order", description = "주문 추가하기")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 회원이 없습니다.")
//    })
//    @Parameter(content = @Content(schema = @Schema(implementation = OrderDto.class)))
//    @PostMapping("/add")
//    public String order(@RequestBody OrderDto orderDto) {
//        Address address = new Address(orderDto.getCity(), orderDto.getStreet(), orderDto.getZipcode());
//        orderService.order(address, orderDto);
//        return "ok";
//    }

    @Operation(summary = "add order", description = "주문 추가하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 회원이 없습니다."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 상품이 없습니다.")
    })
    @Parameter(content = @Content(schema = @Schema(implementation = OrderCouponDto.class)))
    @PostMapping
    public Long order(@RequestBody OrderCouponDto orderCouponDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return orderService.order(orderCouponDto, memberEmail);
    }

    @Operation(summary = "get order", description = "주문 불러오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = OrderReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 회원이 없습니다."),
    })
    @Parameter(name = "memberId", description = "회원 아이디")
    @GetMapping("/{memberId}")
    public Page<OrderReturnDto> getOrder(@PathVariable Long memberId, Pageable pageable) {
        return orderService.getOrderByMember(memberId, pageable);
    }

    @Operation(summary = "cancel order", description = "주문 취소하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 주문이 없습니다."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 이미 취소된 주문입니다.")
    })
    @Parameter(name = "orderId", description = "주문 아이디")
    @DeleteMapping
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "ok";
    }

}
