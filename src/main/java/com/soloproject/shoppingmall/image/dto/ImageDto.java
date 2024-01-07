package com.soloproject.shoppingmall.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ImageDto {

    private long imageId;
    private String imageName;
    private String originalName;
    private String url;
    private Long productId;

}
