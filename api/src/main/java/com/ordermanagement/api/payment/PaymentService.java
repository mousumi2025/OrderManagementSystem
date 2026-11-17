package com.ordermanagement.api.payment;

import java.time.LocalDateTime;
import java.util.List;

import com.ordermanagement.api.common.ResourceNotFoundException;
import com.ordermanagement.api.order.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository repository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<Payment> findAll(Integer orderId) {
        return orderId == null
                ? repository.findAll(Sort.by("paymentId"))
                : repository.findByOrderIdOrderByPaymentId(orderId);
    }

    @Transactional(readOnly = true)
    public Payment findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }

    public Payment create(PaymentRequest request) {
        Payment payment = new Payment();
        apply(payment, request);
        return repository.saveAndFlush(payment);
    }

    public Payment update(Integer id, PaymentRequest request) {
        Payment payment = findById(id);
        apply(payment, request);
        return repository.saveAndFlush(payment);
    }

    public void delete(Integer id) {
        repository.delete(findById(id));
        repository.flush();
    }

    private void apply(Payment payment, PaymentRequest request) {
        if (!orderRepository.existsById(request.orderId())) {
            throw new ResourceNotFoundException("Order", request.orderId());
        }
        payment.setOrderId(request.orderId());
        if (request.paymentDate() != null) {
            payment.setPaymentDate(request.paymentDate());
        } else if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        payment.setAmount(request.amount());
        payment.setPaymentStatus(request.paymentStatus());
        payment.setPaymentMethod(request.paymentMethod());
    }
}
