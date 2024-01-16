package com.soloproject.shoppingmall.product.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.image.dto.ImageDto;
import com.soloproject.shoppingmall.image.entity.Image;
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
    private String description;
    private int price;
    private int stock;
    private long views;
    private List<ImageDto> image;

}
