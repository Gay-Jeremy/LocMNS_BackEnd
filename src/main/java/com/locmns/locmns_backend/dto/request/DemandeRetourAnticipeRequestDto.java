package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DemandeRetourAnticipeRequestDto(
        @NotNull(message = "La date de retour anticipé est obligatoire")
        LocalDate dateRetourAnticipe
) {
}
