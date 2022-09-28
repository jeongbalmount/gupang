package shoppingMall.gupang.web.login;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {

    @Email
    private String email;

    @NotEmpty
    private String password;

}
