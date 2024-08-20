package com.example.demo.user.request;

import com.example.demo.user.domain.UserUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    private final String nickname;
    private final String address;

    @Builder
    public UserUpdateRequest(
        @JsonProperty("nickname") String nickname,
        @JsonProperty("address") String address) {
        this.nickname = nickname;
        this.address = address;
    }

    public UserUpdate to() {
        /* 변환해서 반환하는 메서드 */
        return null;
    }
}
