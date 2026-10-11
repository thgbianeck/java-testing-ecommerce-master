package com.example.ecommerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(FixedClockExtension.class)
class OrderTimestampServiceTest {

    @Test
    @DisplayName("usa o instante fornecido pelo relógio de teste")
    void usaInstanteFixoParaRegistrarPedido(Clock clock) {
        var service = new OrderTimestampService(clock);

        assertThat(service.registeredAt())
                .isEqualTo(Instant.parse("2026-01-15T10:00:00Z"));
    }
}