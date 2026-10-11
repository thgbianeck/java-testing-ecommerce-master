package com.example.ecommerce;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class OrderTimestampService {

    private final Clock clock;

    public OrderTimestampService(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "O relógio não pode ser nulo");
    }

    public Instant registeredAt() {
        return Instant.now(clock);
    }
}