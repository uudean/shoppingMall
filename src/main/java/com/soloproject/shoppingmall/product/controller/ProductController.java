package com.soloproject.shoppingmall.product.controller;

import com.soloproject.shoppingmall.product.dto.ProductPatchDto;
import com.soloproject.shoppingmall.product.dto.ProductPostDto;
import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.mapper.ProductMapper;
import com.soloproject.shoppingmall.product.service.ProductService;
import com.soloproject.shoppingmall.response.MultiResponseDto;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/product")
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/create")
    public ResponseEntity createProduct(@RequestPart ProductPostDto productPostDto,
                                        @RequestPart List<MultipartFile> images) {

        Product product = productService.createProduct(productMapper.productPostDtoToProduct(productPostDto), images);
        ProductResponseDto response = productMapper.productToProductResponseDto(product);

        return new ResponseEntity<>(new SingleResponseDto(response), HttpStatus.CREATED);

    }

    @PatchMapping("/update/{product-id}")
    public ResponseEntity updateProduct(@PathVariable("product-id") long productId,
                                        @RequestBody ProductPatchDto productPatchDto) {

        productPatchDto.setProductId(productId);

        Product product = productService.updateProduct(productMapper.productPatchDtoToProduct(productPatchDto));
        ProductResponseDto response = productMapper.productToProductResponseDto(product);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);

    }

    @GetMapping("/{product-id}")
    public ResponseEntity getProduct(@PathVariable("product-id") long productId) {

        ProductResponseDto response = productService.getProduct(productId);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getProducts(@RequestParam int page,
                                      @RequestParam int size) {

        Page<Product> productPage = productService.getProducts(page - 1, size);
        List<ProductResponseDto> response = productMapper.productToProductResponseDtos(productPage.getContent());

        return new ResponseEntity<>(new MultiResponseDto<>((response), productPage), HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity getCategoryProducts(@RequestParam int page,
                                              @RequestParam int size,
                                              @RequestParam int type,
                                              @RequestParam(value = "category") Product.Category category) throws Exception {

        Page<Product> productPage = productService.getCategoryProducts(page - 1, size, type, category);
        List<ProductResponseDto> response = productMapper.productToProductResponseDtos(productPage.getContent());

        return new ResponseEntity<>(new MultiResponseDto<>((response), productPage), HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity getRecentWatch(@RequestBody List<Long> productIds) {
        List<Product> recentWatch = productService.recentProducts(productIds);
        List<ProductResponseDto> response = productMapper.productToProductResponseDtos(recentWatch);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity deleteProduct(@PathVariable("product-id") long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
