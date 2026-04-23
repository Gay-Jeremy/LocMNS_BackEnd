package com.locmns.locmns_backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmpruntDto(@NotNull(message = "La date de début est obligatoire") LocalDate dateDebut,
                         @NotNull(message = "La date de fin est obligatoire") LocalDate dateFin,
                         int materielId,
                         int utilisateurId){

}
