package com.soloproject.shoppingmall.order.repository;

import com.soloproject.shoppingmall.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByMember_MemberId(PageRequest pageRequest, long memberId);

    @Query("SELECT o FROM Orders o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.member.memberId = :memberId")
    Page<Order> findAllByCreatedAt(PageRequest pageRequest, long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
