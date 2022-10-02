package shoppingMall.gupang.controller.item.dto;

import lombok.Data;

@Data
public class ItemFindDto {
    private Long itemId;
    private int itemCount;

    public ItemFindDto(Long itemId, int itemCount) {
        this.itemId = itemId;
        this.itemCount = itemCount;
    }
}
