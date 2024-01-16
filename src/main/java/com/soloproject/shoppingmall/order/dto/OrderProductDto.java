package com.soloproject.shoppingmall.order.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderProductDto extends Auditable {
    @NotBlank
    private long productId;

    @Min(value = 1,message = "수량은 최소 1개 이상입니다.")
    private int quantity;

}
