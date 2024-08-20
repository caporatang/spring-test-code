package com.example.demo.medium;

import com.example.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.example.demo.controller
 * fileName : UserControllerTest
 * author : taeil
 * date : 8/19/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/19/24        taeil                   최초생성
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_회원_가입을_할_수있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        // given
        UserCreate userCreateDto = UserCreate.builder()
                .email("caporatang-createTest@naver.com")
                .nickname("capo-createTest")
                .address("Busan")
                .build();

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        BDDMockito.given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        BDDMockito.doNothing().when(mailSender).send(any(MimeMessage.class));


        // when
        // then
        mockMvc.perform(
                        post("/api/users")
                                .header("EMAIL", "caporatang@naver.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userCreateDto)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("caporatang-createTest@naver.com"))
                .andExpect(jsonPath("$.nickname").value("capo-createTest"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }


}