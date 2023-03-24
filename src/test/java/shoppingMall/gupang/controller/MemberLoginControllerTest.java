package shoppingMall.gupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import shoppingMall.gupang.domain.Address;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.enums.IsMemberShip;
import shoppingMall.gupang.web.consts.SessionConst;
import shoppingMall.gupang.web.controller.member.dto.MemberDto;
import shoppingMall.gupang.web.exception.AlreadyEmailExistException;
import shoppingMall.gupang.web.exception.AlreadyMemberExistException;
import shoppingMall.gupang.web.exception.LoginFailedException;
import shoppingMall.gupang.web.login.dto.LoginDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

/*
    - 회원 가입
        - 이미 가입한 이메일 존재하는지 확인
        - 회원 가입하기
    - 로그인
    - 로그아웃
 */

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
@Transactional
public class MemberLoginControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mvc;

    private static String BASE_URL = "/member";

    @BeforeEach
    void init() {
        /*
            - MockMvc에 @Autowired만 하면 Spring 애플리케이션 컨텍스트와 연결되지 않을 수 있으며
              세션 관리를 처리하는 데 필요한 구성이 없을 수도 있다.
              그런데 MockMvcBuilders.webAppContextSetup(context).build()를 호출하면
              애플리케이션 컨텍스트와 연결되고 세션 관리를 처리하는 데 필요한 구성이 있는 MockMvc의 새 인스턴스가 생성된다.
              이 새 인스턴스는 세션 관리가 필요한 컨트롤러를 테스트하는 데 사용할 수 있다.
         */
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        Address address = new Address("city", "st", "zip");
        Member member = new Member("email@gmail.com", "password", "name",
                "010-111-111", address, IsMemberShip.MEMBERSHIP);
        em.persist(member);
    }

    /*
        - 이미 가입한 이메일 있으면 AlreadyEmailExistException
        - 없으면 ok
     */
    @Test
    @DisplayName("이메일 체크")
    void emailCheckTest() throws Exception {
        String email = "email@gmail.com";

        // 이메일 이미 존재
        mvc.perform(get(BASE_URL + "/" + email))
                .andExpect(r -> Assertions.assertTrue(r.getResolvedException().getClass()
                        .isAssignableFrom(AlreadyEmailExistException.class)))
                .andExpect(status().is4xxClientError());

        // 새로운 이메일
        String differentEmail = "diffrentEmail@gmail.com";
        mvc.perform(get(BASE_URL + "/" + differentEmail))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원가입 테스트")
    void signupTest() throws Exception {
        MemberDto memberDto = new MemberDto("newEmail@gmail.com", "password", "name", "city",
                "st", "zip", "010-111-111");
        mvc.perform(post(BASE_URL)
                .content(mapper.writeValueAsString(memberDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // 똑같은 이메일의 회원 존재하면 AlreadyMemberExistException
        mvc.perform(post(BASE_URL)
                        .content(mapper.writeValueAsString(memberDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(r -> Assertions.assertTrue(r.getResolvedException().getClass()
                        .isAssignableFrom(AlreadyMemberExistException.class)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인, 로그아웃 테스트")
    void loginLogoutTest() throws Exception {
        LoginDto loginDto = new LoginDto("email@gmail.com", "password");

        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());

        // 로그인 실패
        LoginDto failLoginDto = new LoginDto("email@gmail.com", "password1");
        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(failLoginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(r -> Assertions.assertTrue(r.getResolvedException().getClass()
                        .isAssignableFrom(LoginFailedException.class)))
                .andExpect(status().is4xxClientError());

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(SessionConst.LOGIN_MEMBER, "email@gmail.com");

        // logout
        mvc.perform(post("/logout").session(mockSession))
                .andExpect(status().isOk())
                .andReturn();

        // session 무효화 체크
        assertThat(mockSession.isInvalid()).isTrue();
    }


}
