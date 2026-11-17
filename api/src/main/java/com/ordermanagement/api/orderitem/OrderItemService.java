package com.ordermanagement.api.orderitem;

import java.util.List;

import com.ordermanagement.api.common.ResourceNotFoundException;
import com.ordermanagement.api.order.OrderRepository;
import com.ordermanagement.api.product.Product;
import com.ordermanagement.api.product.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository repository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository repository, OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findAll(Integer orderId) {
        return orderId == null
                ? repository.findAll(Sort.by("orderItemId"))
                : repository.findByOrderIdOrderByOrderItemId(orderId);
    }

    @Transactional(readOnly = true)
    public OrderItem findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order item", id));
    }

    public OrderItem create(OrderItemRequest request) {
        OrderItem item = new OrderItem();
        apply(item, request);
        return repository.saveAndFlush(item);
    }

    public OrderItem update(Integer id, OrderItemRequest request) {
        OrderItem item = findById(id);
        apply(item, request);
        return repository.saveAndFlush(item);
    }

    public void delete(Integer id) {
        repository.delete(findById(id));
        repository.flush();
    }

    private void apply(OrderItem item, OrderItemRequest request) {
        if (!orderRepository.existsById(request.orderId())) {
            throw new ResourceNotFoundException("Order", request.orderId());
        }
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        item.setOrderId(request.orderId());
        item.setProductId(request.productId());
        item.setQuantity(request.quantity());
        item.setUnitPrice(request.unitPrice() != null ? request.unitPrice() : product.getPrice());
    }
}
