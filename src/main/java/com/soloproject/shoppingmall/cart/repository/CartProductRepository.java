package com.soloproject.shoppingmall.cart.repository;

import com.soloproject.shoppingmall.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct,Long> {

    List<CartProduct> findAllByCart_CartId(long cartId);
}
