package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record DroitEmpruntRequestDto(
        @NotNull(message = "La categorie utilisateur est obligatoire")
        Integer categorieUtilisateurId,

        @NotNull(message = "La categorie modele est obligatoire")
        Integer categorieModeleId,

        boolean peutEmprunter
) {}
