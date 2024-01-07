package com.soloproject.shoppingmall.product.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductPostDto extends Auditable {

    private String name;
    private String description;
    private long price;
    private long stock;

}
