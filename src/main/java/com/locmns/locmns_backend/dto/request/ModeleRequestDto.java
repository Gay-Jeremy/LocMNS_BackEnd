package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModeleRequestDto(

        @NotBlank(message = "Le libellé est obligatoire")
        @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
        String libelle,

        @NotNull(message = "La catégorie est obligatoire")
        Integer categorieModeleId
) {}
