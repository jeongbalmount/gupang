package shoppingMall.gupang.controller.item.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "dto")
@Mapping(mappingPath = "elastic/member-mapping.json")
@Setting(settingPath = "elastic/member-setting.json")
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
