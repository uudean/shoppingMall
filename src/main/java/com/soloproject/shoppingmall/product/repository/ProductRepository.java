package com.soloproject.shoppingmall.product.repository;

import com.soloproject.shoppingmall.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p where p.category = :category")
    Page<Product> findByCategory(PageRequest pageRequest, Product.Category category);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByName(String name);
}
