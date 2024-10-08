package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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


public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeUserRepository.save(User.builder()
                        .id(3L)
                        .email("caporatang@naver.com")
                        .nickname("capo")
                        .address("jeonju")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(0L)
                    .build());

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("caporatang2@naver.com")
                .nickname("capoo")
                .address("jeonju")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저_찾아올_수_있다() {
        // given
        String email = "caporatang@naver.com";

        // when
        User result = userService.getByEmail(email);

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
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_PENDING_상태인_유저는_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreate userCreateDto = UserCreate.builder()
                .email("caporatang3333@naver.com")
                .address("Gyeongi")
                .nickname("caporatang3333")
                .build();

        // when
        User result = userService.create(userCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    void userCreateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("Gyeongi")
                .nickname("caporatang3334")
                .build();

        // when
        User result = userService.update(3, userUpdateDto);

        // then
        User userEntity = userService.getById(3);
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
        User user = userService.getById(3);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // given
        //when
        userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        // then
        User userEntity = userService.getById(2);
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