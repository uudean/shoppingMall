package com.soloproject.shoppingmall.product.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.image.dto.ImageDto;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDto extends Auditable {

    private long productId;
    private String name;
    private Product.Category category;
    private String description;
    private int price;
    private int stock;
    private long views;
    private long likeCount;
    private List<ImageDto> image;

}
