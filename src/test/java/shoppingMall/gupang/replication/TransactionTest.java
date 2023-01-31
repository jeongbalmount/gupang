package shoppingMall.gupang.replication;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.replication.domaincontroller.User;
import shoppingMall.gupang.replication.domaincontroller.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TransactionTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private static final String Test_User_Name = "testUser";

    @BeforeEach
    @Transactional
    void before() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("JPA_Transaction_성공_테스트")
    void successJpaCommitTest() throws Exception {
        // given

        // when
        mockMvc.perform(post("/jpa")
                        .param("username", Test_User_Name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Optional<User> user = userRepository.findUserByName(Test_User_Name);
        log.info(String.valueOf(user));
        // then
        assertThat(user.get()).isNotNull();
    }

    @Test
    @DisplayName("JPA_Transaction_롤백_테스트")
    void rollbackJpaTest() {
        // given

        // when
        try {
            mockMvc.perform(post("/jpa/rollback")
                            .param("username", Test_User_Name)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
            ;

        } catch (Exception e) {
            log.error("generate runtime exception to verify transaction rollback");
        }

        Optional<User> user = userRepository.findUserByName(Test_User_Name);

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(user::get);

    }



}
