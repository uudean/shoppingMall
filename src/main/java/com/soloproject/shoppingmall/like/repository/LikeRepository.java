package com.soloproject.shoppingmall.like.repository;

import com.soloproject.shoppingmall.like.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Page<Like> findAllByMember_MemberId(long memberId, PageRequest pageRequest);

    @Query("SELECT l FROM Likes l WHERE l.member.memberId =:memberId AND l.productId = :productId")
    Like findById(long memberId,long productId);
}
