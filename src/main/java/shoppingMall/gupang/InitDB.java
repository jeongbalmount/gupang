package shoppingMall.gupang;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.loginInit();
        initService.cartInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService{

        private final EntityManager em;

        private Member member1;
        private Member member2;

        public void loginInit(){
            Address address1 = new Address("city1", "st1", "zip1");
            Member member1 = new Member("email1@gmail.com", "pwd1", "name1",
                    "010-1111-1111", address1, IsMemberShip.NOMEMBERSHIP);
            em.persist(member1);

            Address address2 = new Address("city2", "st2", "zip2");
            Member member2 = new Member("email2@gmail.com", "pwd2", "name2",
                    "010-2222-2222", address2, IsMemberShip.NOMEMBERSHIP);
            em.persist(member2);

            this.member1 = member1;
            this.member2 = member2;
        }

        public void cartInit() {

            Seller seller = new Seller("010-1111-1111", "managerName");
            Category category = new Category("categoryName");
            Item item1 = new Item("itemName1", 10000, 100, seller, category);
            Item item2 = new Item("itemName2", 20000, 100, seller, category);
            Item item3 = new Item("itemName3", 30000, 100, seller, category);
            Item item4 = new Item("itemName4", 40000, 100, seller, category);
            em.persist(seller);
            em.persist(category);
            em.persist(item1);
            em.persist(item2);
            em.persist(item3);
            em.persist(item4);

            CartItem cartItem1 = new CartItem(member1, item1, 50, item1.getItemPrice());
            CartItem cartItem2 = new CartItem(member1, item2, 50, item2.getItemPrice());
            CartItem cartItem3 = new CartItem(member2, item3, 50, item3.getItemPrice());
            CartItem cartItem4 = new CartItem(member2, item4, 50, item4.getItemPrice());

            em.persist(cartItem1);
            em.persist(cartItem2);
            em.persist(cartItem3);
            em.persist(cartItem4);

        }

    }

}
