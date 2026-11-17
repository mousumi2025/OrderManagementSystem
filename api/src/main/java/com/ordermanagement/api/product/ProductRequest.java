package com.ordermanagement.api.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * @param quantityInStock optional; defaults to 0 when omitted on create
 */
public record ProductRequest(
        @NotBlank @Size(max = 100) String productName,
        String description,
        @NotNull @DecimalMin("0.01") @Digits(integer = 6, fraction = 2) BigDecimal price,
        @PositiveOrZero Integer quantityInStock) {
}
