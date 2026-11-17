package com.ordermanagement.api.order;

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

@Tag(name = "Orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    /** Optionally filter with ?customerId=1 */
    @Operation(summary = "List all orders, optionally filtered by customerId")
    @GetMapping
    public List<Order> list(@RequestParam(required = false) Integer customerId) {
        return service.findAll(customerId);
    }

    @Operation(summary = "Get an order by id")
    @GetMapping("/{id}")
    public Order get(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create an order")
    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody OrderRequest request) {
        Order created = service.create(request);
        return ResponseEntity.created(URI.create("/api/orders/" + created.getOrderId())).body(created);
    }

    @Operation(summary = "Update an order")
    @PutMapping("/{id}")
    public Order update(@PathVariable Integer id, @Valid @RequestBody OrderRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete an order")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
