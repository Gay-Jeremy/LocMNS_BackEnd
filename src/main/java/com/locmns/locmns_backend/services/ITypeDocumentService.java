package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.TypeDocumentRequestDto;
import com.locmns.locmns_backend.dto.response.TypeDocumentResponseDto;

import java.util.List;
import java.util.Optional;

public interface ITypeDocumentService {
    List<TypeDocumentResponseDto> obtenirTousLesTypesDocument();
    Optional<TypeDocumentResponseDto> obtenirTypeDocumentParId(Integer id);
    TypeDocumentResponseDto creerTypeDocument(TypeDocumentRequestDto dto);
    void modifierTypeDocument(Integer id, TypeDocumentRequestDto dto);
    void supprimerTypeDocument(Integer id);
}
