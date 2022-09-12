package shoppingMall.gupang.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.EnumType.STRING;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(STRING)
    private IsMemberShip isMemberShip;

    public Member(String email, String password, String name, String phoneNumber, Address address, IsMemberShip isMemberShip) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isMemberShip = isMemberShip;
    }

    private String cardNumber;

    public void registerMembership() {
        if (this.isMemberShip == IsMemberShip.MEMBERSHIP) {
            throw new IllegalStateException("이미 회원 입니다.");
        }
        this.isMemberShip = IsMemberShip.MEMBERSHIP;
    }

}
