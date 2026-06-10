package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmpruntRequestDto(@NotNull(message = "La date de début est obligatoire") LocalDate dateDebut,
                                @NotNull(message = "La date de fin est obligatoire") LocalDate dateFin,
                                @NotNull Integer materielId,
                                Integer utilisateurId,
                                String note){

}
