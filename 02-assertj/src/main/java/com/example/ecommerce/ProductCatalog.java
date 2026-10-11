package com.example.ecommerce;

import java.util.List;
import java.util.Objects;

public class ProductCatalog {

    public List<Product> findByCategory(
            List<Product> products,
            String category
    ) {
        Objects.requireNonNull(
                products,
                "A lista de produtos não pode ser nula"
        );
        Objects.requireNonNull(
                category,
                "A categoria não pode ser nula"
        );

        return products.stream()
                .filter(product ->
                        product.category().equalsIgnoreCase(category))
                .toList();
    }
}