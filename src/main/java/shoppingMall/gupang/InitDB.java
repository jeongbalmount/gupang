package shoppingMall.gupang;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.domain.IsMemberShip;
import shoppingMall.gupang.domain.Member;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.loginInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService{

        private final EntityManager em;

        public void loginInit(){
            log.info("loginInit!!!");
            log.info("loginInit!!!");
            Address address1 = new Address("city1", "st1", "zip1");
            Member member1 = new Member("email1@gmail.com", "pwd1", "name1",
                    "010-1111-1111", address1, IsMemberShip.NOMEMBERSHIP);
            em.persist(member1);

            Address address2 = new Address("city2", "st2", "zip2");
            Member member2 = new Member("email2@gmail.com", "pwd2", "name2",
                    "010-2222-2222", address2, IsMemberShip.NOMEMBERSHIP);
            em.persist(member2);
        }

    }

}
