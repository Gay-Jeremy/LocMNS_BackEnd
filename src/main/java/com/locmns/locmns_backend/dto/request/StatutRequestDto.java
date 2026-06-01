package com.locmns.locmns_backend.dto.request;

import java.time.LocalDate;

public record StatutRequestDto(
        Integer statutId,
        String code,
        LocalDate date
) {}