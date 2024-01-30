package com.soloproject.shoppingmall.review.repository;

import com.soloproject.shoppingmall.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAllByProduct_ProductId(PageRequest pageRequest,long productId);
}
