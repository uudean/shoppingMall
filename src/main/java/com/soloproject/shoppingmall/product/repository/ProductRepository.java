package com.soloproject.shoppingmall.product.repository;

import com.soloproject.shoppingmall.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByCategory(PageRequest pageRequest, Product.Category category);
}
