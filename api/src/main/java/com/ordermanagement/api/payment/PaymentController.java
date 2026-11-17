package com.ordermanagement.api.payment;

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

@Tag(name = "Payments")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    /** Optionally filter with ?orderId=1 */
    @Operation(summary = "List all payments, optionally filtered by orderId")
    @GetMapping
    public List<Payment> list(@RequestParam(required = false) Integer orderId) {
        return service.findAll(orderId);
    }

    @Operation(summary = "Get a payment by id")
    @GetMapping("/{id}")
    public Payment get(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a payment")
    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody PaymentRequest request) {
        Payment created = service.create(request);
        return ResponseEntity.created(URI.create("/api/payments/" + created.getPaymentId())).body(created);
    }

    @Operation(summary = "Update a payment")
    @PutMapping("/{id}")
    public Payment update(@PathVariable Integer id, @Valid @RequestBody PaymentRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a payment")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
