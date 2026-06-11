package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.StatutMaterielRequestDto;
import com.locmns.locmns_backend.dto.response.StatutMaterielResumeDto;

import java.util.List;
import java.util.Optional;

public interface IStatutMaterielService {
    List<StatutMaterielResumeDto> obtenirTousLesStatutsMateriel();
    Optional<StatutMaterielResumeDto> obtenirStatutMaterielParId(Integer id);
    StatutMaterielResumeDto creerStatutMateriel(StatutMaterielRequestDto dto);
    void modifierStatutMateriel(Integer id, StatutMaterielRequestDto dto);
    void supprimerStatutMateriel(Integer id);
}
