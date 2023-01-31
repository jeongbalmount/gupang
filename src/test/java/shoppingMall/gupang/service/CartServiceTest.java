package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.cart.dto.CartItemMemberDto;
import shoppingMall.gupang.controller.cart.dto.CartItemsMemberDto;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.exception.cart.NoCartItemException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.service.cart.CartService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class CartServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CartService cartService;

    private Member member;
    private List<CartItem> cartItems = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        Address address = new Address("city", "st", "zip");
        Member member = new Member("email@gmail.com", "pwd", "name", "010-1111-1111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(member);

        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
        Item item1 = new Item("itemName1", 1000, 10, seller, category);
        Item item2 = new Item("itemName2", 1000, 10, seller, category);
        Item item3 = new Item("itemName3", 1000, 10, seller, category);
        Item item4 = new Item("itemName4",  1000, 10, seller, category);
        CartItem cartItem1 = new CartItem(member, item1, 10, 1000);
        CartItem cartItem2 = new CartItem(member, item2, 20, 1000);
        CartItem cartItem3 = new CartItem(member, item3, 30, 1000);
        CartItem cartItem4 = new CartItem(member, item4, 40, 1000);


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

        this.member = member;
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);
        cartItems.add(cartItem3);
        cartItems.add(cartItem4);

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
    }

    @Test
    void getAllCartItemsTest() {
        List<CartItem> allCartItems = cartService.getAllCartItems(member.getId());
        for (CartItem i : allCartItems) {
            log.info(String.valueOf(i.getItem().getItemPrice()));
        }
    }

    @Test
    void updateItemCountTest() {
        CartItem cartItem = cartItems.get(0);
        log.info(String.valueOf(cartItem.getId()));
        log.info(String.valueOf(cartItem.getItemCount()));
        cartService.updateItemCount(cartItem.getId(), 35);
        log.info(String.valueOf(cartItem.getId()));
        log.info(String.valueOf(cartItem.getItemCount()));
        Long tmp = 100L;
        Assertions.assertThrows(NoCartItemException.class,() -> cartService.updateItemCount(tmp,35));
    }

    @Test
    void addCartItemTest() {
        for (CartItem i : cartItems) {
            log.info(String.valueOf(i.getId()));
        }
        System.out.println(">>>>>>>>>>>>");
        Item item = items.get(0);
        Assertions.assertThrows(NoItemException.class, () -> cartService.addCartItem(member.getId(),
                item.getId(), 10));
        cartService.addCartItem(member.getId(), item.getId(), 10);
        List<CartItem> allCartItems = cartService.getAllCartItems(member.getId());
        for (CartItem i : allCartItems) {
            log.info(String.valueOf(i.getId()));
        }
    }

    @Test
    void removeCartItemTest() {
        List<Long> ids = new ArrayList<>();
        for (CartItem i : cartItems) {
            ids.add(i.getId());
        }
        List<CartItem> allCartItems2 = cartService.getAllCartItems(member.getId());
        CartItemMemberDto cartItemMemberDto = new CartItemMemberDto();
//        CartItemsMemberDto cartItemsMemberDto = new CartItemsMemberDto(member.getId(), ids);
//        cartService.removeCartItems(cartItemsMemberDto);
        List<CartItem> allCartItems = cartService.getAllCartItems(member.getId());
    }
}
