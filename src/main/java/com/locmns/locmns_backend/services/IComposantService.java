package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.ComposantRequestDto;
import com.locmns.locmns_backend.dto.response.ComposantResponseDto;

import java.util.List;
import java.util.Optional;

public interface IComposantService {
    List<ComposantResponseDto> obtenirTousLesComposants();
    Optional<ComposantResponseDto> obtenirComposantParId(Integer id);
    ComposantResponseDto creerComposant(ComposantRequestDto dto);
    void modifierComposant(Integer id, ComposantRequestDto dto);
    void supprimerComposant(Integer id);
}
