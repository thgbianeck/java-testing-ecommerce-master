package com.example.ecommerce;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductPriceCalculator {

    public BigDecimal calculate(BigDecimal unitPrice, int quantity) {
        Objects.requireNonNull(unitPrice, "O preço unitário não pode ser nulo");

        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa");
        }

        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}