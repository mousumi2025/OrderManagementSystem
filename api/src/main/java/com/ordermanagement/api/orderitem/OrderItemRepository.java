package com.ordermanagement.api.orderitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderIdOrderByOrderItemId(Integer orderId);
}
