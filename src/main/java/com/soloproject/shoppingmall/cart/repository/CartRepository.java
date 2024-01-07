package com.soloproject.shoppingmall.cart.repository;

import com.soloproject.shoppingmall.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findCartByMember_MemberId(long memberId);
}
