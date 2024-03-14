package com.soloproject.shoppingmall.cart.repository;

import com.soloproject.shoppingmall.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct,Long> {

    List<CartProduct> findAllByCart_CartId(long cartId);

    @Query("SELECT c FROM CartProduct c WHERE c.product.productId = :productId AND c.cart.cartId = :cartId")
    CartProduct findCartProduct(long cartId,long productId);
}
