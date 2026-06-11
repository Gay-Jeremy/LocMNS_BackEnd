package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.StatutEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.StatutEmpruntResumeDto;

import java.util.List;
import java.util.Optional;

public interface IStatutEmpruntService {
    List<StatutEmpruntResumeDto> obtenirTousLesStatutsEmprunt();
    List<StatutEmpruntResumeDto> obtenirToutesLesDecisions();
    Optional<StatutEmpruntResumeDto> obtenirStatutEmpruntParId(Integer id);
    StatutEmpruntResumeDto creerStatutEmprunt(StatutEmpruntRequestDto dto);
    void modifierStatutEmprunt(Integer id, StatutEmpruntRequestDto dto);
    void supprimerStatutEmprunt(Integer id);
}
