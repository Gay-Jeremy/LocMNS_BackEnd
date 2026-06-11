package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ComposantRequestDto(
        @NotBlank(message = "Le libelle est obligatoire")
        @Size(min = 2, max = 50, message = "Le libelle doit faire entre 2 et 50 caracteres")
        String libelle,

        List<Integer> categorieModeleIds
) {}
