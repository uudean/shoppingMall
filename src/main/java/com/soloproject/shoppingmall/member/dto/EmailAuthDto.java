package com.soloproject.shoppingmall.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

public class EmailAuthDto {

    @Setter
    @Getter
    public static class signup{
        @Email(message = "올바른 형식의 이메일을 입력해주세요")
        String email;

        @NotBlank
        int authNumber;
    }

    @Setter
    @Getter
    public static class findPassword{

        @Email(message = "올바른 형식의 이메일을 입력해주세요")
        String email;

        @NotBlank
        int authNumber;

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "비밀번호는 8자리 이상 영문자와 숫자를 포함하여야 합니다.")
        String newPassword;
    }
}
