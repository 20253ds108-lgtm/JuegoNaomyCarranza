package com.casino.dados.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RecargaRequest(
        @NotNull(message = "monto obligatorio")
        @Positive(message = "monto mayor a 0") BigDecimal monto
) {
}
