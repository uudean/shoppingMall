package com.soloproject.shoppingmall.review.repository;

import com.soloproject.shoppingmall.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("SELECT r FROM Reviews r WHERE r.product.productId = :productId ORDER BY r.createdAt DESC")
    Page<Review> findAllByProduct_ProductId(PageRequest pageRequest,long productId);

    @Query("SELECT r FROM Reviews r WHERE r.member.memberId = :memberId ORDER BY r.createdAt DESC")
    Page<Review> findAllByMember_MemberId(PageRequest pageRequest,long memberId);
}
