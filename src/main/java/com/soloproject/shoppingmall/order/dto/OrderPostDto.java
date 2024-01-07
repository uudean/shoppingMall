package com.soloproject.shoppingmall.order.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderPostDto extends Auditable {
    private long memberId;
    private List<OrderProductDto> orderProducts;
}
