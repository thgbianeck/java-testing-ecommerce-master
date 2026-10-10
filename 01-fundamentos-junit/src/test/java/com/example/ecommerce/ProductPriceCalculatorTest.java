package com.example.ecommerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductPriceCalculatorTest {

    @Test
    @DisplayName("Calcula o total multiplicando preço unitário pela quantidade")
    void deveCalcularTotalDoProduto() {
        // Arrange
        var calculator = new ProductPriceCalculator();
        var unitPrice = new BigDecimal("19.90");
        var quantity = 3;

        // Act
        var total = calculator.calculate(unitPrice, quantity);

        // Assert
        assertEquals(new BigDecimal("59.70"), total);
    }

    @Test
    @DisplayName("Calcula o total com quantidade zero")
    void deveCalcularTotalComQuantidadeZero() {
        // Arrange
        var calculator = new ProductPriceCalculator();
        var unitPrice = new BigDecimal("19.90");
        var quantity = 0;

        // Act
        var total = calculator.calculate(unitPrice, quantity);

        // Assert
        assertEquals(new BigDecimal("0.00"), total);
    }

    @Test
    @DisplayName("Lança exceção quando a quantidade é negativa")
    void deveLancarExcecaoQuandoQuantidadeEhNegativa() {
        // Arrange
        var calculator = new ProductPriceCalculator();
        var unitPrice = new BigDecimal("19.90");
        var quantity = -3;

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(unitPrice, quantity);
        });

        // Assert
        assertEquals("A quantidade não pode ser negativa", exception.getMessage());
    }

    @Test
    @DisplayName("Lança exceção quando o preço unitário é negativo")
    void deveLancarExcecaoQuandoPrecoUnitarioEhNegativo() {
        // Arrange
        var calculator = new ProductPriceCalculator();
        var unitPrice = new BigDecimal("-19.90");
        var quantity = 3;

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(unitPrice, quantity);
        });

        // Assert
        assertEquals("O preço unitário não pode ser negativo", exception.getMessage());
    }

    @Test
    @DisplayName("Lança exceção quando o preço unitário é nulo")
    void deveLancarExcecaoQuandoPrecoUnitarioEhNulo() {
        // Arrange
        var calculator = new ProductPriceCalculator();
        BigDecimal unitPrice = null;
        var quantity = 3;

        // Act
        var exception = assertThrows(NullPointerException.class, () -> {
            calculator.calculate(unitPrice, quantity);
        });

        // Assert
        assertEquals("O preço unitário não pode ser nulo", exception.getMessage());
    }
    
}
