package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.dto.response.ModeleResumeDto;

import java.util.List;
import java.util.Optional;

public interface IModeleService {
    List<ModeleResumeDto> obtenirTousLesModeles();

    List<ModeleResumeDto> obtenirModeleParCategorie(int categorieModeleId);

    Optional<ModeleResumeDto> obtenirModeleParId(Integer id);

    ModeleResumeDto creerModele(ModeleRequestDto dto);

    void modifierModele(Integer id, ModeleRequestDto dto);

    void supprimerModele(Integer id);
}
