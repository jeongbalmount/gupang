package shoppingMall.gupang.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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

    @BeforeEach
    void init() {

    }

}
