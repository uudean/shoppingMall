package com.soloproject.shoppingmall.image.repository;

import com.soloproject.shoppingmall.image.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.product.productId = :productId")
    Optional<Image> findByProductId(long productId);

    @Query("SELECT i FROM Image i WHERE i.product.productId = null")
    Page<Image> findAllByProductIdIsNull(PageRequest pageRequest);
}
