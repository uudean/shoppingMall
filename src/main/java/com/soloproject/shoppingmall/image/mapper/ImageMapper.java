package com.soloproject.shoppingmall.image.mapper;

import com.soloproject.shoppingmall.image.dto.ImageDto;
import com.soloproject.shoppingmall.image.entity.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto imageToImageDto(Image image);
}
