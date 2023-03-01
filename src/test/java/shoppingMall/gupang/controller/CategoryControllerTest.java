package shoppingMall.gupang.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.service.category.CategoryService;
import shoppingMall.gupang.service.item.ItemService;
import shoppingMall.gupang.web.controller.category.CategoryController;
import shoppingMall.gupang.web.controller.category.dto.CategoryDto;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
@Transactional
public class CategoryControllerTest {

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String BASE_URL = "/category";

    @BeforeEach
    void init() {
        /*
        elasticsearch의 검색기능을 사용하려면 itemService의 addItem을 통해 item을 등록해야
        elasticsearch에도 저장되기 때문에 itemService를 통해 item을 등록해야 한다.
         */
      Category category = new Category("food");
      Seller seller = new Seller("010-111-222", "manager");
      Item item1 = new Item("apple", 1000, 100, seller, category);
      Item item2 = new Item("banana", 1000, 100, seller, category);
      Item item3 = new Item("orange", 1000, 100, seller, category);

      em.persist(category);
      em.persist(seller);
      em.persist(item1);
      em.persist(item2);
      em.persist(item3);

    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    void addCategoryTest() throws Exception {

        CategoryDto categoryDto = new CategoryDto("food");

        mvc.perform(post("/category/add")
                        .content(toJson(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 속한 상품 불러오기 테스트")
    void getCategoryItemsTest() throws Exception {
        /*
        CategoryController 리턴 타입 List<ItemReturnDto>로 바꿔야 test 통과
         */
        String name = "food";
        MvcResult result = mvc.perform(get(BASE_URL + "/" +name))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        JSONObject o1 = jsonArray.getJSONObject(0);
        JSONObject o2 = jsonArray.getJSONObject(1);
        JSONObject o3 = jsonArray.getJSONObject(2);

        Assertions.assertThat("apple").isEqualTo(o1.getString("name"));
        Assertions.assertThat("1000").isEqualTo(o1.getString("price"));

        Assertions.assertThat("banana").isEqualTo(o2.getString("name"));
        Assertions.assertThat("orange").isEqualTo(o3.getString("name"));
    }

    private <T> String toJson(T data) throws JsonProcessingException {
        return new Gson().toJson(data);
    }
}
