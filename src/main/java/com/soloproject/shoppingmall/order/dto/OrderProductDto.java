package com.soloproject.shoppingmall.order.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderProductDto extends Auditable {

    private long productId;
    private long quantity;

}
