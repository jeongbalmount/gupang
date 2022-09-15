package shoppingMall.gupang.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.domain.IsMemberShip;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

@SpringBootTest
@Slf4j
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void memberRepositoryTest() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("email", "pwd", "name!!!", "010-1111-1111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(member);

        Optional<Member> emailMember = memberRepository.findByEmail("email");
        Member m = emailMember.orElse(null);
        if (m == null) {
            log.info("null!");
            return;
        }
        log.info(m.getName());
    }

}
