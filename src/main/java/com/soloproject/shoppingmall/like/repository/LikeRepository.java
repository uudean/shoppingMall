package com.soloproject.shoppingmall.like.repository;

import com.soloproject.shoppingmall.like.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Page<Like> findAllByMember_MemberId(long memberId, PageRequest pageRequest);
}
