package com.soloproject.shoppingmall.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPatchDto {

    @NotNull(message = "상품ID는 필수 입력값 입니다.")
    private long productId;

    private int quantity;
}
