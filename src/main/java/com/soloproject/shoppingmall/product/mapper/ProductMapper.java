package com.soloproject.shoppingmall.product.mapper;

import com.soloproject.shoppingmall.product.dto.ProductPatchDto;
import com.soloproject.shoppingmall.product.dto.ProductPostDto;
import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.product.entity.Product;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productPostDtoToProduct(ProductPostDto productPostDto);

    Product productPatchDtoToProduct(ProductPatchDto productPatchDto);

    ProductResponseDto productToProductResponseDto(Product product);

    List<ProductResponseDto> productToProductResponseDtos(List<Product> products);
}
