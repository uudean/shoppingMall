package com.soloproject.shoppingmall.order.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderResponseDto extends Auditable {
    private long orderId;
    private long memberId;
    private long totalPrice;
    private List<OrderProductResponseDto> orderProducts;
}
