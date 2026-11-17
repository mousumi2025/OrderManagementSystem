package com.ordermanagement.api.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByOrderIdOrderByPaymentId(Integer orderId);
}
