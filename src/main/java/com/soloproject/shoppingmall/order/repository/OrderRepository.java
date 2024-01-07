package com.soloproject.shoppingmall.order.repository;

import com.soloproject.shoppingmall.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
