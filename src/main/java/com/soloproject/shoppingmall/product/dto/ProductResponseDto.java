package com.soloproject.shoppingmall.product.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDto extends Auditable {

    private long productId;
    private String name;
    private String description;
    private long price;
    private long stock;
    private Image image;

}
