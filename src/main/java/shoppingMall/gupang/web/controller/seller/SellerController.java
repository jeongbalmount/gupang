package shoppingMall.gupang.web.controller.seller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.web.controller.cart.dto.CartItemMemberItemDto;
import shoppingMall.gupang.web.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.service.seller.SellerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
@Tag(name = "seller", description = "판매자 api")
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "get seller items", description = "판매자 판매 상품들 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ItemReturnDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 판매자가 없습니다.")
    })
    @GetMapping("/{sellerId}")
    public List<ItemReturnDto> getSellerItems(@Parameter(description = "seller id", required = true)
                                                  @PathVariable Long sellerId) {
        List<ItemReturnDto> collect = sellerService.getSellerItems(sellerId).stream()
                .map(i -> new ItemReturnDto(i.getName(), i.getItemPrice(), i.getSeller().getManagerName(),
                        i.getCategory().getName(), i.getId()))
                .collect(Collectors.toList());

        return collect;
    }

    @Operation(summary = "add serller", description = "판매자 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @Parameters({
            @Parameter(name = "managerName", description = "판매 매니저 이름"),
            @Parameter(name = "sellerNumber", description = "판매처 전화번호")
    })
    @PostMapping
    public String addSeller(@RequestParam(value = "managerName") String managerName,
                            @RequestParam(value = "sellerNumber") String sellerNumber) {
        sellerService.registerSeller(managerName, sellerNumber);
        return "ok";
    }

    @Operation(summary = "add serller", description = "판매자 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST => 해당 판매자가 없습니다.")
    })
    @Parameters({
            @Parameter(name = "sellerId", description = "판매자 아이디"),
            @Parameter(name = "managerName", description = "판매 매니저 이름")
    })
    @PatchMapping
    public String editSellerInfo(@RequestParam(value = "sellerId") Long sellerId,
                                 @RequestParam(value = "managerName") String managerName) {
        sellerService.updateManagerName(sellerId, managerName);
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
