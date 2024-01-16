package com.soloproject.shoppingmall.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPatchDto {
    private long productId;
    private String name;
    private String description;
    private int price;
    private int stock;
}
