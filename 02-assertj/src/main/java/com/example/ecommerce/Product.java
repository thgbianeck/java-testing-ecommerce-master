package com.example.ecommerce;

import java.math.BigDecimal;
import java.util.Objects;

public record Product(
        String sku,
        String name,
        String category,
        BigDecimal price
) {
    public Product {
        Objects.requireNonNull(sku, "O SKU não pode ser nulo");
        Objects.requireNonNull(name, "O nome não pode ser nulo");
        Objects.requireNonNull(category, "A categoria não pode ser nula");
        Objects.requireNonNull(price, "O preço não pode ser nulo");

        if (sku.isBlank()) {
            throw new IllegalArgumentException("O SKU não pode estar em branco");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("O nome não pode estar em branco");
        }

        if (price.signum() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo");
        }
    }
}