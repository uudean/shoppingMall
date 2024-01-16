package com.soloproject.shoppingmall.order.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderPostDto extends Auditable {

    @NotBlank
    private long memberId;

    @NotBlank
    private List<OrderProductDto> orderProducts;
}
