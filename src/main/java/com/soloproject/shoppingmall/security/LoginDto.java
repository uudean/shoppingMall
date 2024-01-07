package com.soloproject.shoppingmall.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String email;
    private String password;

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long memberId;
        private String email;
        private String accessToken;
    }
}
