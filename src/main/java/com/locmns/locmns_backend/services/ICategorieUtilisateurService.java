package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.CategorieUtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResumeDto;

import java.util.List;
import java.util.Optional;

public interface ICategorieUtilisateurService {
    List<CategorieUtilisateurResumeDto> obtenirToutesLesCategoriesUtilisateur();
    Optional<CategorieUtilisateurResumeDto> obtenirCategorieUtilisateurParId(Integer id);
    CategorieUtilisateurResumeDto creerCategorieUtilisateur(CategorieUtilisateurRequestDto dto);
    void modifierCategorieUtilisateur(Integer id, CategorieUtilisateurRequestDto dto);
    void supprimerCategorieUtilisateur(Integer id);
}
