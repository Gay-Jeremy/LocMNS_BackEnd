package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StatutMaterielRequestDto(
        @NotBlank(message = "Le libelle est obligatoire")
        @Size(min = 2, max = 50, message = "Le libelle doit faire entre 2 et 50 caracteres")
        String libelle,

        @Size(max = 50, message = "Le code ne doit pas depasser 50 caracteres")
        String code
) {}
