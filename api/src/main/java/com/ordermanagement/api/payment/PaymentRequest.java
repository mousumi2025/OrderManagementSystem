package com.ordermanagement.api.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @param paymentDate optional; defaults to now on create, unchanged on update
 */
public record PaymentRequest(
        @NotNull Integer orderId,
        LocalDateTime paymentDate,
        @NotNull @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal amount,
        @NotNull @Pattern(regexp = "Pending|Paid|Failed|Refunded",
                message = "must be one of: Pending, Paid, Failed, Refunded") String paymentStatus,
        @NotNull @Pattern(regexp = "Cash|Credit Card|Debit Card|Mobile Pay",
                message = "must be one of: Cash, Credit Card, Debit Card, Mobile Pay") String paymentMethod) {
}
