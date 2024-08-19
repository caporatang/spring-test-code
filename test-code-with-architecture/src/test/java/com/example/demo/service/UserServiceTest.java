package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

/**
 * packageName : com.example.demo.service
 * fileName : UserServiceTest
 * author : taeil
 * date : 8/18/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/18/24        taeil                   최초생성
 */

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저_찾아올_수_있다() {
        // given
        String email = "caporatang@naver.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("capo");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저_찾아올_수_없다() {
        // given
        String email = "caporatang2@naver.com";

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_PENDING_상태인_유저는_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("caporatang3333@naver.com")
                .address("Gyeongi")
                .nickname("caporatang3333")
                .build();

        // 메일 발송 부분 강의 내용과 다른 이슈가 있어 mock 설정을 다르게..
        // MimeMessage Mock 객체 생성 및 설정
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        BDDMockito.given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // MimeMessageHelper 내부에서 사용하는 mailSender.send()가 호출될 때 아무 동작도 하지 않도록 설정
        BDDMockito.doNothing().when(mailSender).send(any(MimeMessage.class));

        // when
        UserEntity result = userService.create(userCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // assertThat(result.getCertificationCode()).isEqualTo("T.T"); // FIXME
    }

    @Test
    void userCreateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("Gyeongi")
                .nickname("caporatang3334")
                .build();

        // when
        UserEntity result = userService.update(3, userUpdateDto);

        // then
        UserEntity userEntity = userService.getById(3);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Gyeongi");
        assertThat(userEntity.getNickname()).isEqualTo("caporatang3334");
    }


    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // given
        //when
        userService.login(3);

        // then
        UserEntity userEntity = userService.getById(3);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        // assertThat(result.getLastLoginAt()).isEqualTo("T.T"); // FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // given
        //when
        userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        // then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }



}