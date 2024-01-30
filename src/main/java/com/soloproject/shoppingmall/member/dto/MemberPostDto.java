package com.soloproject.shoppingmall.member.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberPostDto extends Auditable {

    @NotBlank
    @Email(message = "올바른 형식의 이메일을 입력해주세요" )
    private String email;

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
