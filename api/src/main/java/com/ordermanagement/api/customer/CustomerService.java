package com.ordermanagement.api.customer;

import java.util.List;

import com.ordermanagement.api.common.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return repository.findAll(Sort.by("customerId"));
    }

    @Transactional(readOnly = true)
    public Customer findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    public Customer create(CustomerRequest request) {
        Customer customer = new Customer();
        apply(customer, request);
        return repository.saveAndFlush(customer);
    }

    public Customer update(Integer id, CustomerRequest request) {
        Customer customer = findById(id);
        apply(customer, request);
        return repository.saveAndFlush(customer);
    }

    public void delete(Integer id) {
        repository.delete(findById(id));
        repository.flush();
    }

    private static void apply(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
    }
}
