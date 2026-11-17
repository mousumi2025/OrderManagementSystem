package com.ordermanagement.api.product;

import java.util.List;

import com.ordermanagement.api.common.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return repository.findAll(Sort.by("productId"));
    }

    @Transactional(readOnly = true)
    public Product findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return repository.saveAndFlush(product);
    }

    public Product update(Integer id, ProductRequest request) {
        Product product = findById(id);
        apply(product, request);
        return repository.saveAndFlush(product);
    }

    public void delete(Integer id) {
        repository.delete(findById(id));
        repository.flush();
    }

    private static void apply(Product product, ProductRequest request) {
        product.setProductName(request.productName());
        product.setDescription(request.description());
        product.setPrice(request.price());
        if (request.quantityInStock() != null) {
            product.setQuantityInStock(request.quantityInStock());
        } else if (product.getQuantityInStock() == null) {
            product.setQuantityInStock(0);
        }
    }
}
