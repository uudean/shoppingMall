package com.soloproject.shoppingmall.member.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateDto {
    private long memberId;

    @NotNull
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "비밀번호는 8자리 이상 영문자와 숫자를 포함하여야 합니다.")
    private String newPassword;

    @NotNull
    private String confirmPassword;
}
