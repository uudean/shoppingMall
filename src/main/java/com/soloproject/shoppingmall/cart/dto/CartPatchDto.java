package com.soloproject.shoppingmall.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPatchDto {
    private long productId;
    private long quantity;
}
