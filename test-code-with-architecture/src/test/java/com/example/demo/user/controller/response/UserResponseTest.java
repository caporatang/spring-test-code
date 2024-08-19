package com.example.demo.user.controller.response;

import com.example.demo.user.domain.MyProfileResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.user.controller.response
 * fileName : UserResponse
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
public class UserResponseTest {

    @Test
    public void User로_응답을_생성할_수_있다() {

            // given
            User user = User.builder()
                    .id(1L)
                    .email("caporatang@naver.com")
                    .nickname("caporatang")
                    .address("Seoul")
                    .status(UserStatus.ACTIVE)
                    .lastLoginAt(100L)
                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .build();

            // when
            UserResponse userResponse = UserResponse.from(user);

            // then
            assertThat(userResponse.getId()).isEqualTo(1);
            assertThat(userResponse.getEmail()).isEqualTo("caporatang@naver.com");
            assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
            assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);

    }
}