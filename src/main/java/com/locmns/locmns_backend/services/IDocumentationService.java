package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.DocumentationRequestDto;
import com.locmns.locmns_backend.dto.response.DocumentationResponseDto;

import java.util.List;
import java.util.Optional;

public interface IDocumentationService {
    List<DocumentationResponseDto> obtenirToutesLesDocumentations();
    Optional<DocumentationResponseDto> obtenirDocumentationParId(Integer id);
    DocumentationResponseDto creerDocumentation(DocumentationRequestDto dto);
    void modifierDocumentation(Integer id, DocumentationRequestDto dto);
    void supprimerDocumentation(Integer id);
}
