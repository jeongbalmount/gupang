package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.member.AlreadyMemberException;
import shoppingMall.gupang.service.member.MemberService;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
public class MemberServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberService memberService;
    private Member member;

    @BeforeEach
    void beforeEach() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("email@gmail.com", "pwd", "name", "010-1111-1111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(member);
        this.member = member;
    }

    @Test
    void changeMembershipStatusTest() {
        log.info(String.valueOf(member.getIsMemberShip()));
        memberService.changeMemberShipStatus(member.getId());
        log.info(String.valueOf(member.getIsMemberShip()));
        Assertions.assertThrows(AlreadyMemberException.class, () -> memberService.changeMemberShipStatus(member.getId()));
    }

//    @Test
//    void memberOutServiceTest() {
//        memberService.memberOutService(member.getId());
//        assertThat(memberService.getMember(member.getId())).isEqualTo(null);
//    }
}
