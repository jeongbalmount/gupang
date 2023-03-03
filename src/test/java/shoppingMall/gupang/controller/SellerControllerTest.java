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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;

import javax.persistence.EntityManager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Slf4j
public class SellerControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/seller";

    private Seller seller;

    private List<Item> items = new ArrayList<>();

    @BeforeEach
    void before() {
        Category category = new Category("food");
        Seller seller = new Seller("010-111-111", "managerName");
        em.persist(seller);
        em.persist(category);
        this.seller = seller;

        Item item1 = new Item("apple", 1000, 100, seller, category);
        Item item2 = new Item("banana", 1000, 100, seller, category);
        Item item3 = new Item("orange", 1000, 100, seller, category);

        em.persist(seller);
        em.persist(category);
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

        mvc.perform(patch(BASE_URL)
                .params(map)
        ).andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

}
