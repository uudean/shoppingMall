package com.soloproject.shoppingmall.order.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderResponseDto extends Auditable {
    private long orderId;
    private long memberId;
    private int totalPrice;
    private Order.OrderStatus orderStatus;
    private List<OrderProductResponseDto> orderProducts;
}
