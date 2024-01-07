package com.soloproject.shoppingmall.cart.repository;

import com.soloproject.shoppingmall.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct,Long> {
}
