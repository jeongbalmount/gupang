package shoppingMall.gupang.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.AlreadyMemberException;

import javax.persistence.*;

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

    private String cardNumber;

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

    public void registerMembership() {
        if (this.isMemberShip == IsMemberShip.MEMBERSHIP) {
            throw new AlreadyMemberException("이미 회원 입니다.");
        }
        this.isMemberShip = IsMemberShip.MEMBERSHIP;
    }

    public void registerCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

}
