package com.locmns.locmns_backend.dto.response;

public record DroitEmpruntResponseDto(
        Integer categorieUtilisateurId,
        Integer categorieModeleId,
        boolean peutEmprunter,
        CategorieUtilisateurResumeDto categorieUtilisateur,
        CategorieModeleResumeDto categorieModele
) {}
