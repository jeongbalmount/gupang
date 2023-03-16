package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shoppingMall.gupang.domain.*;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.repository.cartItem.CartRepository;
import shoppingMall.gupang.service.cart.CartService;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.cart.CartController;
import shoppingMall.gupang.web.controller.cart.dto.CartItemIdsDto;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    - 카트 물품 보여주기
    - 카트에 담은 상품중 하나 개수 수정하기
    - 카트에 담은 상품 여러개(1개 포함) 삭제하기
    - 카트에 새로운 상품 추가하기(개수 여러개 가능)
 */
@SpringBootTest
@Transactional
@Slf4j
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CartController cartController;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private LoginInterceptor loginInterceptor;

    private Item item;
    private Member member;
    private List<Long> cartItemIds = new ArrayList<>();

    private String BASE_URL = "/cart";

    @BeforeEach
    void init() {

        this.mvc = MockMvcBuilders.standaloneSetup(this.cartController)
                .addInterceptors(this.loginInterceptor)
                .build();

        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");

        Address address = new Address("city", "st", "zip");
        Member member = new Member("test@test.com", "password", "name", "010-111-111",
                address, IsMemberShip.NOMEMBERSHIP);

        em.persist(seller);
        em.persist(category);
        em.persist(member);
        this.member = member;

        Item item = new Item("itme name", 1000, 100, seller, category);
        em.persist(item);
        this.item = item;

        for (int i = 1; i < 6; i ++) {
            CartItem cartItem = new CartItem(member, item, i, item.getItemPrice());
            em.persist(cartItem);
            this.cartItemIds.add(cartItem.getId());
        }
    }

    @Test
    @DisplayName("상품 카트에 넣기 테스트")
    void addItemToCartTest() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("itemId", item.getId().toString());
        map.set("count", "2");

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.member.getEmail());

        mvc.perform(post(BASE_URL)
                        .params(map)
                        .session(mockSession)
                ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원의 카트 물품들 반환하는 테스트")
    void getMemberCartItems() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.member.getEmail());

        MvcResult result = mvc.perform(get(BASE_URL)
                        .session(mockSession)
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);

        Long itemId = item.getId();
        for (int i=0; i < jsonArray.length(); i ++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Assertions.assertThat(itemId).isEqualTo(jsonObject.getLong("itemId"));
        }
    }

    @Test
    @DisplayName("카트 물품 구매 개수 수정 테스트")
    void updateCartItemCount() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("cartItemId", String.valueOf(cartItemIds.get(4)));
        map.set("count", "10");

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.member.getEmail());

        mvc.perform(put(BASE_URL)
                .params(map)
                .session(mockSession)
        ).andExpect(status().isOk());

        Optional<CartItem> optionalCartItem = cartRepository.findById(cartItemIds.get(4));
        CartItem cartItem = optionalCartItem.orElseThrow();
        Assertions.assertThat(cartItem.getItemCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("카트 물품 삭제 테스트")
    void deleteCartItemTest() throws Exception {
        // 저장한 cartItem중 첫번째와 마지막 cartItem 삭제
        List<Long> deleteIds = new ArrayList<>();
        deleteIds.add(cartItemIds.get(0));
        deleteIds.add(cartItemIds.get(4));
        CartItemIdsDto cartItemIdsDto = new CartItemIdsDto(deleteIds);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.member.getEmail());

        mvc.perform(delete(BASE_URL)
                .content(mapper.writeValueAsString(cartItemIdsDto))
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockSession)
        ).andExpect(status().isOk());

        // 5개 cartItem중 중간 1-3까지 cartItem만 존재
        // 그냥 subList만 사용할 경우 불필요한 캐싱이 될 수 있기 때문에 new ArrayList<>로 감싸기
        List<Long> afterDeleteIds = new ArrayList<>(cartItemIds.subList(1, 4));
        List<CartItem> getCartItems = cartService.getAllCartItems(member.getEmail());
        List<Long> newIds = new ArrayList<>();
        for (CartItem cartItem : getCartItems) {
            newIds.add(cartItem.getId());
        }

        // 새로 가져온 cartItem들이 중간 1-3까지의 cartItem들의 id와 같아야 한다.
        Assertions.assertThat(afterDeleteIds.containsAll(newIds)).isTrue();
    }

}
