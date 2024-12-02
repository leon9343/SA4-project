package ch.usi.inf.bsc.sa4.lab02spring.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SimulationParameters(
        @PositiveOrZero
        @NotNull
        int wage,
        @NotNull
        @PositiveOrZero
        double transportCost,

        @PositiveOrZero
        Double globalRentLimit) {
}
