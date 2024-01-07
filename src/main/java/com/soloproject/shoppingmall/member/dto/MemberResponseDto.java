package com.soloproject.shoppingmall.member.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemberResponseDto extends Auditable {
    private long memberId;
    private String email;
    private String name;
    private String password;
    private String address;
    private String phone;
}
