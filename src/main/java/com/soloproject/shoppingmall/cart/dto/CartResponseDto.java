package com.soloproject.shoppingmall.cart.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class CartResponseDto extends Auditable {
    private long cartId;
    private long memberId;
    private int totalPrice;
    private List<CartProductResponseDto> cartProducts;
}
