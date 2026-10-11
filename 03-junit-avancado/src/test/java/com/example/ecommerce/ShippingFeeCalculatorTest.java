package com.example.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Cálculo do frete")
class ShippingFeeCalculatorTest {

    private ShippingFeeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ShippingFeeCalculator();
    }

    @Nested
    @DisplayName("quando o subtotal é menor que R$ 100,00")
    class SubtotalAbaixoDoLimite {

        @ParameterizedTest
        @ValueSource(strings = {"0.00", "25.00", "99.99"})
        @DisplayName("cobra o frete padrão")
        void cobraFretePadrao(String subtotal) {
            var actual = calculator.calculate(new BigDecimal(subtotal));

            assertThat(actual)
                    .isEqualByComparingTo("9.90");
        }
    }

    @Nested
    @DisplayName("quando o subtotal alcança o limite")
    class FreteGratis {

        @ParameterizedTest
        @CsvSource({
                "100.00, 0.00",
                "125.50, 0.00",
                "250.00, 0.00"
        })
        @DisplayName("retorna frete grátis no limite ou acima dele")
        void retornaFreteGratis(
                String subtotal,
                String freteEsperado
        ) {
            var actual = calculator.calculate(new BigDecimal(subtotal));

            assertThat(actual)
                    .isEqualByComparingTo(freteEsperado);
        }
    }

    @Nested
    @DisplayName("quando a entrada é inválida")
    class EntradaInvalida {

        @Test
        @DisplayName("rejeita subtotal nulo")
        void rejeitaSubtotalNulo() {
            assertThatThrownBy(() -> calculator.calculate(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("O subtotal não pode ser nulo");
        }
    }

    @ParameterizedTest(name = "subtotal negativo {0} deve ser rejeitado")
    @MethodSource("subtotaisNegativos")
    @DisplayName("rejeita qualquer subtotal negativo")
    void rejeitaSubtotalNegativo(BigDecimal subtotal) {
        assertThatThrownBy(() -> calculator.calculate(subtotal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O subtotal não pode ser negativo");
    }

    static Stream<Arguments> subtotaisNegativos() {
        return Stream.of(
                arguments(new BigDecimal("-0.01")),
                arguments(new BigDecimal("-25.00"))
        );
    }
}