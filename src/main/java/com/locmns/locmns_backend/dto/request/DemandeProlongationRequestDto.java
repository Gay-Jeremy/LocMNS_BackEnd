package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DemandeProlongationRequestDto(
        @NotNull(message = "La date de prolongation est obligatoire")
        LocalDate dateProlongation
) {
}
