package com.example.ecommerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

class ProductCatalogTest {

    private final ProductCatalog catalog = new ProductCatalog();

    @Test
    @DisplayName("Filtra produtos por categoria sem diferenciar maiúsculas")
    void deveFiltrarProdutosPorCategoria() {
        // Arrange
        var products = List.of(
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal("12.50")),
                new Product("CHA-002", "Chá verde", "Bebidas", new BigDecimal("8.90")),
                new Product("PAP-003", "Caderno", "Papelaria", new BigDecimal("15.00"))
        );

        // Act
        var result = catalog.findByCategory(products, "bebidas");

        // Assert
        assertThat(result)
                .hasSize(2)
                .extracting(Product::name)
                .containsExactly("Café especial", "Chá verde");
    }

    @Test
    @DisplayName("Extrai SKU e preço dos produtos encontrados")
    void deveExporOsCamposRelevantesDosProdutosEncontrados() {
        // Arrange
        var products = List.of(
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal("12.50")),
                new Product("CHA-002", "Chá verde", "Bebidas", new BigDecimal("8.90"))
        );

        // Act
        var result = catalog.findByCategory(products, "Bebidas");

        // Assert
        assertThat(result)
                .extracting(Product::sku, Product::price)
                .containsExactly(
                        tuple("CAF-001", new BigDecimal("12.50")),
                        tuple("CHA-002", new BigDecimal("8.90"))
                );
    }

    @Test
    @DisplayName("Retorna uma lista vazia quando não há produtos na categoria")
    void deveRetornarListaVaziaQuandoNaoHaCorrespondencias() {
        // Arrange
        var products = List.of(
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal("12.50"))
        );

        // Act
        var result = catalog.findByCategory(products, "Papelaria");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Rejeita lista de produtos nula com mensagem clara")
    void deveRejeitarListaNula() {
        // Act e Assert
        assertThatThrownBy(() -> catalog.findByCategory(null, "Bebidas"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("A lista de produtos não pode ser nula");
    }

    @Test
    @DisplayName("Rejeita categoria nula com mensagem clara")
    void deveRejeitarCategoriaNula() {
        // Arrange
        var products = List.of(
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal("12.50"))
        );

        // Act e Assert
        assertThatThrownBy(() -> catalog.findByCategory(products, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("A categoria não pode ser nula");
    }

    // ----------------------------------------------------------------
    // Novos testes
    // ----------------------------------------------------------------

    @Test
    @DisplayName("Retorna lista vazia quando a lista de produtos está vazia")
    void deveRetornarListaVaziaQuandoListaDeProdutosEstaVazia() {
        // Arrange
        List<Product> products = List.of();

        // Act
        var result = catalog.findByCategory(products, "Bebidas");

        // Assert
        // Lista vazia: não há ordem a verificar
        assertThat(result).isEmpty();
    }

    @ParameterizedTest(name = "categoria \"{0}\" encontra os mesmos produtos")
    @ValueSource(strings = {"BEBIDAS", "bebidas", "bEbIdAs", "Bebidas"})
    @DisplayName("Encontra os mesmos produtos com qualquer combinação de maiúsculas e minúsculas")
    void deveEncontrarOsMesmosProdutosIndependenteDaCaixaDaCategoria(String categoria) {
        // Arrange
        var products = List.of(
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal("12.50")),
                new Product("PAP-003", "Caderno", "Papelaria", new BigDecimal("15.00")),
                new Product("CHA-002", "Chá verde", "Bebidas", new BigDecimal("8.90"))
        );

        // Act
        var result = catalog.findByCategory(products, categoria);

        // Assert
        // Ordem NÃO verificada: o foco é a equivalência de caixa, não a ordenação
        assertThat(result)
                .extracting(Product::sku, Product::name)
                .containsExactlyInAnyOrder(
                        tuple("CAF-001", "Café especial"),
                        tuple("CHA-002", "Chá verde")
                );
    }

    @Test
    @DisplayName("Rejeita categoria nula mesmo quando a lista de produtos está vazia")
    void deveRejeitarCategoriaNulaComListaVazia() {
        // Arrange
        List<Product> products = List.of();

        // Act e Assert
        // Mensagem verificada: já é contrato nos testes existentes de categoria nula
        assertThatThrownBy(() -> catalog.findByCategory(products, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("A categoria não pode ser nula");
    }

    @Test
    @DisplayName("Preserva a ordem original dos produtos e expõe SKU e nome")
    void devePreservarOrdemOriginalAoExporSkuENome() {
        // Arrange
        var products = List.of(
                new Product("CHA-002", "Chá verde", "Bebidas", new BigDecimal("8.90")),
                new Product("PAP-003", "Caderno", "Papelaria", new BigDecimal("15.00")),
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal("12.50"))
        );

        // Act
        var result = catalog.findByCategory(products, "bebidas");

        // Assert
        // Ordem VERIFICADA: o catálogo deve manter a sequência de entrada
        assertThat(result)
                .extracting(Product::sku, Product::name)
                .containsExactly(
                        tuple("CHA-002", "Chá verde"),
                        tuple("CAF-001", "Café especial")
                );
    }

    @ParameterizedTest(name = "SKU \"{0}\" é rejeitado")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Rejeita SKU em branco")
    void deveRejeitarSkuEmBranco(String sku) {
        // Act e Assert
        // Somente o tipo: a mensagem não faz parte da expectativa deste cenário
        assertThatThrownBy(() ->
                new Product(sku, "Café especial", "Bebidas", new BigDecimal("12.50")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "nome \"{0}\" é rejeitado")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Rejeita nome em branco")
    void deveRejeitarNomeEmBranco(String nome) {
        // Act e Assert
        assertThatThrownBy(() ->
                new Product("CAF-001", nome, "Bebidas", new BigDecimal("12.50")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "preço {0} é rejeitado")
    @ValueSource(strings = {"-0.01", "-1", "-100.00"})
    @DisplayName("Rejeita preço negativo")
    void deveRejeitarPrecoNegativo(String preco) {
        // Act e Assert
        assertThatThrownBy(() ->
                new Product("CAF-001", "Café especial", "Bebidas", new BigDecimal(preco)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Aceita preço zero (fronteira da regra de preço negativo)")
    void deveAceitarPrecoZero() {
        // Act e Assert
        assertThatCode(() ->
                new Product("CAF-001", "Café especial", "Bebidas", BigDecimal.ZERO))
                .doesNotThrowAnyException();
    }
}