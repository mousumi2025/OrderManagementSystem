package com.ordermanagement.api.orderitem;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @param unitPrice optional; defaults to the product's current price when omitted
 */
public record OrderItemRequest(
        @NotNull Integer orderId,
        @NotNull Integer productId,
        @NotNull @Positive Integer quantity,
        @DecimalMin("0.01") @Digits(integer = 6, fraction = 2) BigDecimal unitPrice) {
}
