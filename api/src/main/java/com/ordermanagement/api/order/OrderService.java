package com.ordermanagement.api.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ordermanagement.api.common.ResourceNotFoundException;
import com.ordermanagement.api.customer.CustomerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository repository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll(Integer customerId) {
        return customerId == null
                ? repository.findAll(Sort.by("orderId"))
                : repository.findByCustomerIdOrderByOrderId(customerId);
    }

    @Transactional(readOnly = true)
    public Order findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    public Order create(OrderRequest request) {
        Order order = new Order();
        apply(order, request);
        return repository.saveAndFlush(order);
    }

    public Order update(Integer id, OrderRequest request) {
        Order order = findById(id);
        apply(order, request);
        return repository.saveAndFlush(order);
    }

    /** Deleting an order also deletes its order_items (ON DELETE CASCADE). */
    public void delete(Integer id) {
        repository.delete(findById(id));
        repository.flush();
    }

    private void apply(Order order, OrderRequest request) {
        if (!customerRepository.existsById(request.customerId())) {
            throw new ResourceNotFoundException("Customer", request.customerId());
        }
        order.setCustomerId(request.customerId());
        order.setOrderDate(firstNonNull(request.orderDate(), order.getOrderDate(), LocalDateTime.now()));
        order.setStatus(firstNonNull(request.status(), order.getStatus(), "Pending"));
        order.setTotalAmount(firstNonNull(request.totalAmount(), order.getTotalAmount(), BigDecimal.ZERO));
    }

    private static <T> T firstNonNull(T requested, T current, T fallback) {
        return requested != null ? requested : current != null ? current : fallback;
    }
}
