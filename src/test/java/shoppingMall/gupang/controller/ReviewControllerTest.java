package shoppingMall.gupang.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Slf4j
public class ReviewControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/review/";



}
