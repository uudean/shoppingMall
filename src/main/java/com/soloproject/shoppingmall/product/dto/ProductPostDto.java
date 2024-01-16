package com.soloproject.shoppingmall.product.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductPostDto extends Auditable {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private int price;

    @NotNull
    private int stock;

}
