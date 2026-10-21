package com.casino.dados.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IniciarPartidaRequest(
        @NotNull(message = "el id debe ser obligatorio") Long jugadorId,
        @NotNull(message = "apuesta obligatoria")
        @Positive(message = "apuesta no negativa") BigDecimal apuesta
) {
}
