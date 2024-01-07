package com.soloproject.shoppingmall.cart.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartPostDto {
    @NotNull(message = "사용자ID는 필수 입력값 입니다.")
    private long memberId;

    @NotNull(message = "상품ID는 필수 입력값 입니다.")
    private long productId;

    @Min(value = 1, message = "수량은 최소 1개 이상입니다.")
    private long quantity;
}
