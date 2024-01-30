package com.soloproject.shoppingmall.order.repository;

import com.soloproject.shoppingmall.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findAllByMember_MemberId(PageRequest pageRequest,long memberId);
}
