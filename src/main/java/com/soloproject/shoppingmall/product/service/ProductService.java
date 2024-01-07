package com.soloproject.shoppingmall.product.service;

import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Product product) {

        Product findProduct = findProduct(product.getProductId());

        Optional.ofNullable(product.getName()).ifPresent(findProduct::setName);
        Optional.ofNullable(product.getDescription()).ifPresent(findProduct::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(findProduct::setPrice);
        Optional.ofNullable(product.getStock()).ifPresent(findProduct::setStock);
        Optional.ofNullable(product.getModifiedAt()).ifPresent(localDateTime -> findProduct.setModifiedAt(LocalDateTime.now()));

        return productRepository.save(findProduct);

    }

    @Transactional(readOnly = true)
    public Product getProduct(long productId) {
        return findProduct(productId);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAll(pageRequest);
    }

    @Transactional
    public void deleteProduct(long productId) {
        Product product = findProduct(productId);
        productRepository.delete(product);
    }

    private Product findProduct(long productId) {
        return productRepository.findById(productId).orElseThrow();
    }
}
