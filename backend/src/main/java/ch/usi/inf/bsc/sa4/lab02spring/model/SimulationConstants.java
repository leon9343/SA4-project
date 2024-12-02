package ch.usi.inf.bsc.sa4.lab02spring.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SimulationConstants(
        @NotNull
        @PositiveOrZero
        int minUtility,
        @NotNull
        @PositiveOrZero
        double alpha,
        @NotNull
        @PositiveOrZero
        double costZero,
        @NotNull
        @PositiveOrZero
        double delta,
        @NotNull
        @PositiveOrZero
        int rentEdge) {
}
