package shoppingMall.gupang.controller.item.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "dto")
@Mapping(mappingPath = "elastic/item-mapping.json")
@Setting(settingPath = "elastic/item-setting.json")
public class ItemSearchDto {

    @Id
    private String id;

    private Long itemid;

    private String itemname;

    private int price;

    private String category;

    public ItemSearchDto(Long itemId, String itemName, int price, String category) {
        this.itemid = itemId;
        this.itemname = itemName;
        this.price = price;
        this.category = category;
    }
}
