package com.soloproject.shoppingmall.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String username;
    private String password;
    private String address;
    private String phone;
}
