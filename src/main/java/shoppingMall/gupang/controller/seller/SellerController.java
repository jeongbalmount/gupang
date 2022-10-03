package shoppingMall.gupang.controller.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shoppingMall.gupang.controller.item.dto.ItemReturnDto;
import shoppingMall.gupang.service.seller.SellerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping
    public Result getSellerItems(Long sellerId) {
        List<ItemReturnDto> collect = sellerService.getSellerItems(sellerId).stream()
                .map(i -> new ItemReturnDto(i.getName(), i.getItemPrice(), i.getSeller().getManagerName(),
                        i.getCategory().getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping("/add")
    public String addSeller(@RequestParam(value = "managerName") String managerName,
                            @RequestParam(value = "sellerNumber") String sellerNumber) {
        sellerService.registerSeller(managerName, sellerNumber);
        return "ok";
    }

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
