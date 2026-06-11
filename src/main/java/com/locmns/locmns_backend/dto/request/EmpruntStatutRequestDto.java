package com.locmns.locmns_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmpruntStatutRequestDto(Integer id, @NotBlank(message = "Le code statut est obligatoire") String code) {
}
