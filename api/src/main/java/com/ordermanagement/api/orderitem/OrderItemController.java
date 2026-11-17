package com.ordermanagement.api.orderitem;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Items")
@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    /** Optionally filter with ?orderId=1 */
    @Operation(summary = "List all order items, optionally filtered by orderId")
    @GetMapping
    public List<OrderItem> list(@RequestParam(required = false) Integer orderId) {
        return service.findAll(orderId);
    }

    @Operation(summary = "Get an order item by id")
    @GetMapping("/{id}")
    public OrderItem get(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create an order item")
    @PostMapping
    public ResponseEntity<OrderItem> create(@Valid @RequestBody OrderItemRequest request) {
        OrderItem created = service.create(request);
        return ResponseEntity.created(URI.create("/api/order-items/" + created.getOrderItemId())).body(created);
    }

    @Operation(summary = "Update an order item")
    @PutMapping("/{id}")
    public OrderItem update(@PathVariable Integer id, @Valid @RequestBody OrderItemRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete an order item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
