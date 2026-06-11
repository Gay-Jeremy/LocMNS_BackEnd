package com.locmns.locmns_backend.dto.response;

import java.util.List;

public record DocumentationResponseDto(
        Integer id,
        String titre,
        String url,
        String format,
        TypeDocumentResponseDto typeDocument,
        List<ModeleResumeDto> modeles
) {}
