package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DocumentationRequestDto(
        @NotBlank(message = "Le titre est obligatoire")
        @Size(min = 2, max = 100, message = "Le titre doit faire entre 2 et 100 caracteres")
        String titre,

        @NotBlank(message = "L'URL est obligatoire")
        @Size(max = 255, message = "L'URL ne doit pas depasser 255 caracteres")
        String url,

        @Size(max = 50, message = "Le format ne doit pas depasser 50 caracteres")
        String format,

        @NotNull(message = "Le type de document est obligatoire")
        Integer typeDocumentId,

        List<Integer> modeleIds
) {}
