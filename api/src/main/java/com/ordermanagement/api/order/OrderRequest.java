package com.ordermanagement.api.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Optional fields fall back to the database defaults on create:
 * orderDate = now, status = Pending, totalAmount = 0.
 * On update, omitted optional fields keep their current values.
 */
public record OrderRequest(
        @NotNull Integer customerId,
        LocalDateTime orderDate,
        @Pattern(regexp = "Pending|Preparing|Completed|Cancelled",
                message = "must be one of: Pending, Preparing, Completed, Cancelled") String status,
        @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal totalAmount) {
}
