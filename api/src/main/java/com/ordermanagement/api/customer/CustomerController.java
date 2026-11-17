package com.ordermanagement.api.customer;

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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customers")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(summary = "List all customers")
    @GetMapping
    public List<Customer> list() {
        return service.findAll();
    }

    @Operation(summary = "Get a customer by id")
    @GetMapping("/{id}")
    public Customer get(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a customer")
    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerRequest request) {
        Customer created = service.create(request);
        return ResponseEntity.created(URI.create("/api/customers/" + created.getCustomerId())).body(created);
    }

    @Operation(summary = "Update a customer")
    @PutMapping("/{id}")
    public Customer update(@PathVariable Integer id, @Valid @RequestBody CustomerRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a customer")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
