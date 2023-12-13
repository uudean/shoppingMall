package com.soloproject.shoppingmall.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberPatchDto {

    @NotBlank
    @Size(min = 2, max = 12, message = "이름은 2자 이상 12자 이하여야 합니다.")
    private String name;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "비밀번호는 8자리 이상 영문자와 숫자를 포함하여야 합니다.")
    private String password;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

}
