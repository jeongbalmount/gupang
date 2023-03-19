package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.web.controller.item.dto.ItemDto;

import javax.persistence.EntityManager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
    -  seller login member login과 세션 분리 필요
 */

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Slf4j
public class SellerControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String BASE_URL = "/seller";

    private Seller seller;

    private List<Item> items = new ArrayList<>();

    private Long categoryId;
    private Long sellerId;

    @BeforeEach
    void before() {
        Category category = new Category("food");
        Seller seller = new Seller("010-111-111", "managerName");
        this.seller = seller;

        Item item1 = new Item("apple", 1000, 100, seller, category);
        Item item2 = new Item("banana", 1000, 100, seller, category);
        Item item3 = new Item("orange", 1000, 100, seller, category);

        em.persist(seller);
        em.persist(category);

        categoryId = category.getId();
        sellerId = seller.getId();

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        items.add(item1);
        items.add(item2);
        items.add(item3);

    }

    @Test
    @DisplayName("판매자 판매 물품 리턴 테스트")
    void getSellerItemsTest() throws Exception {
        MvcResult result = mvc.perform(get(BASE_URL + "/" + seller.getId())
                ).andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);

        JSONObject o1 = jsonArray.getJSONObject(0);
        JSONObject o2 = jsonArray.getJSONObject(1);
        JSONObject o3 = jsonArray.getJSONObject(2);
        String[] values = {"apple", "banana", "orange"};
        Assertions.assertTrue(Arrays.asList(values).contains(o1.getString("name")));
        Assertions.assertTrue(Arrays.asList(values).contains(o2.getString("name")));
        Assertions.assertTrue(Arrays.asList(values).contains(o3.getString("name")));
    }

    @Test
    @DisplayName("새로운 판매자 추가 테스트")
    void addSellerTest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("managerName", "name");
        map.set("sellerNumber", "010-111-111");

        mvc.perform(post(BASE_URL)
                        .params(map)
                ).andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    @DisplayName("판매자 정보 업데이트 테스트")
    void editSellerTest() throws Exception {
        String id = seller.getId().toString();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("sellerId", id);
        map.set("managerName", "name");

        mvc.perform(put(BASE_URL)
                .params(map)
        ).andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

        /*
        - 상품을 추가하는 행위는 itemController에서 진행할 수 없을것이다.
        - seller가 추가 하기 때문에 sellerController에서 진행되어야 한다.
        - 그리고 Interceptor 분리해주는게 맞다.
        - (addInterceptor는 "/item/**"라는 경로가 있다면 /item이후 **에 대해선 http method 상관없이 모두 적용된다.)
     */
    @Test
    @DisplayName("새로운 상품 추가 테스트")
    void addItemTest() throws Exception {
//        MockHttpSession mockSession = new MockHttpSession();
//        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.member.getEmail());
        ItemDto dto = new ItemDto("new apple", 1000, 100, sellerId, categoryId);

        mvc.perform(post(BASE_URL + "/" + "newitem")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
//                        .session(mockSession)
                ).andExpect(status().isOk());
    }

}
