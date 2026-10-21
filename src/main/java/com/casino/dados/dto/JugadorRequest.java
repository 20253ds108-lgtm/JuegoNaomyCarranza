package com.casino.dados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record JugadorRequest(
        @NotBlank(message = "el nombre es obligatorio") String nombre,
        @PositiveOrZero(message = "saldo inicial debe de ser positivo") BigDecimal saldoInicial
) {
}
