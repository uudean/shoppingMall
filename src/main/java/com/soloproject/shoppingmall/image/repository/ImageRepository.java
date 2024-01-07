package com.soloproject.shoppingmall.image.repository;

import com.soloproject.shoppingmall.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.product.productId = :productId")
    Optional<Image> findByProductId(long productId);
}
