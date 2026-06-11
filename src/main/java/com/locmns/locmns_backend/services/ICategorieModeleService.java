package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.CategorieModeleRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieModeleResumeDto;

import java.util.List;
import java.util.Optional;

public interface ICategorieModeleService {
    List<CategorieModeleResumeDto> obtenirToutesLesCategoriesModele();
    Optional<CategorieModeleResumeDto> obtenirCategorieModeleParId(Integer id);
    CategorieModeleResumeDto creerCategorieModele(CategorieModeleRequestDto dto);
    void modifierCategorieModele(Integer id, CategorieModeleRequestDto dto);
    void supprimerCategorieModele(Integer id);
}
