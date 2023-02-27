package shoppingMall.gupang.web.controller.member;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberDto {

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;

    @NotEmpty
    private String street;

    @NotEmpty
    private String zipcode;

    @NotEmpty
    private String phoneNumber;

}
