package shoppingMall.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    @Embedded
    private Address address;

    @NotEmpty
    private String phoneNumber;

    private MemberStatus membershipStatus;

    private String cardNumber;

}
