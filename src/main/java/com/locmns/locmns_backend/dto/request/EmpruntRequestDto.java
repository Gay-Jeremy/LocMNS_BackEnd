package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmpruntRequestDto(
        @NotNull(message = "La date de début est obligatoire") LocalDate dateDebut,
        @NotNull(message = "La date de fin est obligatoire") LocalDate dateFin,
        @NotNull(message = "Le matériel est obligatoire")
        @Positive(message = "Identifiant de matériel invalide")
        Integer materielId,
        Integer utilisateurId,
        @Size(max = 255, message = "La note ne peut pas dépasser 255 caractères")
        String note){

}
