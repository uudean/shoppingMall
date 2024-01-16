package com.soloproject.shoppingmall.product.mapper;

import com.soloproject.shoppingmall.image.dto.ImageDto;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.product.dto.ProductPatchDto;
import com.soloproject.shoppingmall.product.dto.ProductPostDto;
import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productPostDtoToProduct(ProductPostDto productPostDto);

    Product productPatchDtoToProduct(ProductPatchDto productPatchDto);

    default ProductResponseDto productToProductResponseDto(Product product) {

        if (product == null) {
            return null;
        }

        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setCreatedAt(product.getCreatedAt());
        productResponseDto.setModifiedAt(product.getModifiedAt());
        productResponseDto.setProductId(product.getProductId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setStock(product.getStock());
        productResponseDto.setViews(product.getViews());

        List<Image> images = product.getImages();
        List<ImageDto> imageDtos = imagesToImageDtos(images);

        productResponseDto.setImage(imageDtos);

        return productResponseDto;

    }
    default List<ImageDto> imagesToImageDtos(List<Image> images) {
        if ( images == null ) {
            return null;
        }

        List<ImageDto> list = new ArrayList<ImageDto>( images.size() );
        for ( Image image : images ) {
            list.add( imageToImageDto( image ) );
        }

        return list;
    }
    default ImageDto imageToImageDto(Image image) {
        if ( image == null ) {
            return null;
        }

        ImageDto imageDto = new ImageDto();

        imageDto.setImageId( image.getImageId() );
        imageDto.setImageName( image.getImageName() );
        imageDto.setOriginalName( image.getOriginalName() );
        imageDto.setProductId(image.getProduct().getProductId());
        imageDto.setUrl( image.getUrl() );

        return imageDto;
    }

    List<ProductResponseDto> productToProductResponseDtos(List<Product> products);
}
