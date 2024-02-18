package com.soloproject.shoppingmall.member.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberPatchDto extends Auditable {

    private long memberId;

    @Size(min = 2, max = 12, message = "이름은 2자 이상 12자 이하여야 합니다.")
    private String name;

    private String address;

    private String phone;

}
