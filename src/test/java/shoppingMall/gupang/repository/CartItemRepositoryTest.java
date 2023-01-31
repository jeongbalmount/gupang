package shoppingMall.gupang.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.cartItem.CartRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class CartItemRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CartRepository cartItemRepository;

    private List<CartItem> cartItems = new ArrayList<>();

    private Member member;

    @BeforeEach
    void beforeEach() {
        Address address = new Address("city", "street", "zip");
        Member member = new Member("1@gmail.com", "123", "name", "010-1111-2222",
                address, IsMemberShip.NOMEMBERSHIP);
        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
        Item item1 = new Item("name",100, 10, seller, category);
        Item item2 = new Item("name",200, 10, seller, category);
        Item item3 = new Item("name",300,  10, seller, category);
        Item item4 = new Item("name",400,  10, seller, category);
        CartItem cartItem1 = new CartItem(member, item1, 10, 1000);
        CartItem cartItem2 = new CartItem(member, item2, 10, 1000);
        CartItem cartItem3 = new CartItem(member, item3, 10, 1000);
        CartItem cartItem4 = new CartItem(member, item4, 10, 1000);


        em.persist(member);
        em.persist(seller);
        em.persist(category);
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.persist(cartItem1);
        em.persist(cartItem2);
        em.persist(cartItem3);
        em.persist(cartItem4);
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);
        cartItems.add(cartItem3);
        cartItems.add(cartItem4);
        this.member = member;
    }

    @Test
    void cartItemRepositoryTest() {
        List<CartItem> cartItems = cartItemRepository.findCartItemsByMemberId(this.member.getId());

        for (CartItem cartItem : cartItems) {
            log.info(String.valueOf(cartItem.getItem().getItemPrice()));
        }
    }
}
