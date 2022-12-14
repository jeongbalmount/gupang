package shoppingMall.gupang.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.member.AlreadyMemberException;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "memberIndex", columnList = "email"))
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
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
