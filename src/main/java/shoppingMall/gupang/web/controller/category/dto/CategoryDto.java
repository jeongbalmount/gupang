package shoppingMall.gupang.web.controller.category.dto;


import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {

    @NotNull
    public String name;

    public CategoryDto(String name) {
        this.name = name;
    }
}
