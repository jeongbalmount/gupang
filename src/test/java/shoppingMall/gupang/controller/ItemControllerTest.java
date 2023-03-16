package shoppingMall.gupang.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.elasticsearch.itemSearch.ItemSearchRepository;
import shoppingMall.gupang.service.item.ItemService;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.config.WebConfig;
import shoppingMall.gupang.web.controller.item.ItemController;
import shoppingMall.gupang.web.controller.item.dto.ItemDto;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Slf4j
public class ItemControllerTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemSearchRepository itemSearchRepository;
    @Autowired
    private ItemController itemController;
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private ObjectMapper mapper;

    private Long sellerId;

    private Long categoryId;

    private Member member;

    private static final String BASE_URL = "/item/";


    @BeforeEach
    void init() {

        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
//        this.mvc = MockMvcBuilders.standaloneSetup(this.itemController)
//                .addInterceptors(this.loginInterceptor)
//                .build();

        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");

        em.persist(seller);
        em.persist(category);
        itemService.saveItem(new ItemDto("apple", 1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("banana", 1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("yellow banana",
                1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("the real sweat orange",
                1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("orange jeju!",
                1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("사과", 1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("바나나", 1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("맛좋은 제주 오렌지",
                1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("오렌지는 맛있어!",
                1000, 100, seller.getId(), category.getId()));
        itemService.saveItem(new ItemDto("사과의 고장에서 직접 딴 리얼사과",
                1000, 100, seller.getId(), category.getId()));

        sellerId = seller.getId();
        categoryId = category.getId();

        Address address = new Address("city", "st", "zip");
        Member member = new Member("test@test.com", "password", "name", "010-111-111",
                address, IsMemberShip.NOMEMBERSHIP);
        em.persist(member);
        this.member = member;
    }

    @AfterEach
    void after() {
        itemSearchRepository.deleteAll();
    }

    @Test
    @DisplayName("새로운 상품 추가 테스트")
    void addItemTest() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, this.member.getEmail());
        ItemDto dto = new ItemDto("new apple", 1000, 100, sellerId, categoryId);

        mvc.perform(post(BASE_URL)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
//                        .session(mockSession)
                ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("한개의 상품 상세 정보 반환 테스트")
    void getSingleItemTest() throws Exception {
        ItemDto dto = new ItemDto("new apple", 1000, 100, sellerId, categoryId);
        MvcResult result = mvc.perform(post(BASE_URL)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        long itemId = jsonObject.getLong("itemId");

        MvcResult otherResult = mvc.perform(get(BASE_URL + "singleitem/" + itemId))
                .andExpect(status().isOk())
                .andReturn();

        String content2 = otherResult.getResponse().getContentAsString();
        JSONArray jsonArray2 = new JSONArray(content2);
        JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
        Assertions.assertThat(jsonObject2.getString("itemName")).isEqualTo("new apple");
    }

    @Test
    @DisplayName("상품 영어 검색 서비스 테스트")
    void englishSearchItemTest() throws Exception {
        String w1 = "orange";
        String w2 = "oraneg";
        String w3 = "the real orange";

        MvcResult result = mvc.perform(get(BASE_URL + w3))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
//        JSONObject o1 = jsonArray.getJSONObject(0);
//        JSONObject o2 = jsonArray.getJSONObject(1);
//        String[] values = {"orange jeju!", "the real sweat orange"};
//        Assertions.assertThat(Arrays.asList(values).contains(o1.getString("itemname")))
//                .isEqualTo(true);
//        Assertions.assertThat(Arrays.asList(values).contains(o2.getString("itemname")))
//                .isEqualTo(true);

        JSONObject o1 = jsonArray.getJSONObject(0);
        String[] values = {"the real sweat orange"};
        Assertions.assertThat(Arrays.asList(values).contains(o1.getString("itemname")))
                .isEqualTo(true);

    }

    @Test
    @DisplayName("상품 한글 검색 서비스 테스트")
    void hangulSearchItemTest() throws  Exception{
        String w1 = "오렌지";
        String w2 = "오랜지";
        String w3 = "사과의고장";

        MockHttpServletResponse response = mvc.perform(get(BASE_URL + w3))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // 한글 안깨지게 하려면 밑의 설정 필요
        response.setCharacterEncoding("UTF-8");
        String content = response.getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
//        JSONObject o1 = jsonArray.getJSONObject(0);
//        JSONObject o2 = jsonArray.getJSONObject(1);
//
//        String[] values = {"오렌지는 맛있어!", "맛좋은 제주 오렌지"};
//        Assertions.assertThat(Arrays.asList(values).contains(o1.getString("itemname")))
//                .isEqualTo(true);
//        Assertions.assertThat(Arrays.asList(values).contains(o2.getString("itemname")))
//                .isEqualTo(true);

//        JSONObject o1 = jsonArray.getJSONObject(0);
//        JSONObject o2 = jsonArray.getJSONObject(1);
//
//        String[] values = {"오렌지는 맛있어!", "맛좋은 제주 오렌지"};
//        Assertions.assertThat(Arrays.asList(values).contains(o1.getString("itemname")))
//                .isEqualTo(true);
//        Assertions.assertThat(Arrays.asList(values).contains(o2.getString("itemname")))
//                .isEqualTo(true);
        JSONObject o1 = jsonArray.getJSONObject(0);
        JSONObject o2 = jsonArray.getJSONObject(1);

        String[] values = {"사과", "사과의 고장에서 직접 딴 리얼사과"};
        Assertions.assertThat(Arrays.asList(values).contains(o1.getString("itemname")))
                .isEqualTo(true);
        Assertions.assertThat(Arrays.asList(values).contains(o2.getString("itemname")))
                .isEqualTo(true);
    }



}
