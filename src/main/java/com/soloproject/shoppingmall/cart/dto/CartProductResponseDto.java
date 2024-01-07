package com.soloproject.shoppingmall.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartProductResponseDto{
    private long productId;
    private long quantity;
}
