package com.soloproject.shoppingmall.product.dto;

import com.soloproject.shoppingmall.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPatchDto {
    private long productId;
    private String name;
    private Product.Category category;
    private String description;
    private int price;
    private int stock;
}
