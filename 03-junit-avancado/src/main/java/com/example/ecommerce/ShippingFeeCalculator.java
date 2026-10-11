package com.example.ecommerce;

import java.math.BigDecimal;
import java.util.Objects;

public class ShippingFeeCalculator {

    private static final BigDecimal FREE_SHIPPING_THRESHOLD =
            new BigDecimal("100.00");

    private static final BigDecimal STANDARD_SHIPPING_FEE =
            new BigDecimal("9.90");

    public BigDecimal calculate(BigDecimal subtotal) {
        Objects.requireNonNull(subtotal, "O subtotal não pode ser nulo");

        if (subtotal.signum() < 0) {
            throw new IllegalArgumentException(
                    "O subtotal não pode ser negativo"
            );
        }

        if (subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO.setScale(2);
        }

        return STANDARD_SHIPPING_FEE;
    }
}