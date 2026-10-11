package com.example.ecommerce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class DiscountCalculator {

    private static final BigDecimal STANDARD_THRESHOLD =
            new BigDecimal("100.00");

    private static final BigDecimal PREMIUM_THRESHOLD =
            new BigDecimal("500.00");

    private static final BigDecimal STANDARD_RATE =
            new BigDecimal("0.10");

    private static final BigDecimal PREMIUM_RATE =
            new BigDecimal("0.15");

    private static final int MONEY_SCALE = 2;

    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        Objects.requireNonNull(
                subtotal,
                "O subtotal não pode ser nulo"
        );

        if (subtotal.signum() < 0) {
            throw new IllegalArgumentException(
                    "O subtotal não pode ser negativo"
            );
        }

        BigDecimal rate = rateFor(subtotal);

        return subtotal
                .multiply(rate)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal rateFor(BigDecimal subtotal) {
        if (subtotal.compareTo(PREMIUM_THRESHOLD) >= 0) {
            return PREMIUM_RATE;
        }

        if (subtotal.compareTo(STANDARD_THRESHOLD) >= 0) {
            return STANDARD_RATE;
        }

        return BigDecimal.ZERO;
    }
}