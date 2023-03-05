package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/*
    - 새로운 리뷰 등록
    - 상품에 맞는 리뷰 불러오기
        - 첫 페이지 불러오기
        - 두번째 페이지 불러오기
    - 리뷰 삭제
    - 리뷰 수정
    - 리뷰 추천
 */

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Slf4j
public class ReviewControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String BASE_URL = "/review/";

    private Item item;

    @BeforeEach
    void init() {
        Seller seller = new Seller("010-111-222", "manager");
        Category category = new Category("food");

        em.persist(seller);
        em.persist(category);
        Item item = new Item("item name", 1000, 100, seller, category);
        em.persist(item);
        this.item = item;
    }

    @Test
    void addReviewTest() throws Exception {
        ReviewDto dto = new ReviewDto(item.getId(), "the title", "contents");
        mvc.perform(post(BASE_URL + "add")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }



}
