package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.SalleRequestDto;
import com.locmns.locmns_backend.dto.response.SalleResumeDto;

import java.util.List;
import java.util.Optional;

public interface ISalleService {
    List<SalleResumeDto> obtenirToutesLesSalles();
    Optional<SalleResumeDto> obtenirSalleParId(Integer id);
    SalleResumeDto creerSalle(SalleRequestDto dto);
    void modifierSalle(Integer id, SalleRequestDto dto);
    void supprimerSalle(Integer id);
}
