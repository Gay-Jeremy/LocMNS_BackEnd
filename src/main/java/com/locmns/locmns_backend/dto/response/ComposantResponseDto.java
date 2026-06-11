package com.locmns.locmns_backend.dto.response;

import java.util.List;

public record ComposantResponseDto(
        Integer id,
        String libelle,
        List<CategorieModeleResumeDto> categoriesModeles
) {}
