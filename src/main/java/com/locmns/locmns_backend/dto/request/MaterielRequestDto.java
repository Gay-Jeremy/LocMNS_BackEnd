package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record MaterielRequestDto(

        @NotBlank(message = "Le numéro de série est obligatoire")
        @Size(min = 2, max = 100, message = "Le numéro de série doit faire entre 2 et 100 caractères")
        String numSerie,

        @NotNull(message = "Le modèle est obligatoire")
        Integer modeleId,

        Integer statutMaterielId,

        Integer salleId

) {
}
