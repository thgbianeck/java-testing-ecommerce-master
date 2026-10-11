package com.example.ecommerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cálculo do desconto do pedido")
class DiscountCalculatorTest {

    private final DiscountCalculator calculator =
            new DiscountCalculator();

    @ParameterizedTest(name = "subtotal {0} gera desconto {1}")
    @CsvSource({
            "0.00,    0.00",
            "99.99,   0.00",
            "100.00, 10.00",
            "250.00, 25.00",
            "499.99, 50.00",
            "500.00, 75.00",
            "333.33, 33.33"
    })
    @DisplayName("calcula o desconto conforme a faixa e o arredondamento")
    void calculaDescontoPorFaixa(
            String subtotalText,
            String expectedDiscountText
    ) {
        var subtotal = new BigDecimal(subtotalText);

        var discount = calculator.calculateDiscount(subtotal);

        assertThat(discount)
                .isEqualByComparingTo(expectedDiscountText);
    }

    @Test
    @DisplayName("rejeita subtotal nulo")
    void rejeitaSubtotalNulo() {
        assertThatThrownBy(() -> calculator.calculateDiscount(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("O subtotal não pode ser nulo");
    }

    @Test
    @DisplayName("rejeita subtotal negativo")
    void rejeitaSubtotalNegativo() {
        assertThatThrownBy(() ->
                calculator.calculateDiscount(new BigDecimal("-0.01")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O subtotal não pode ser negativo");
    }
}