package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.CategorieUtilisateur;

public record CategorieUtilisateurResumeDto(Integer id, String libelle) {

    public static CategorieUtilisateurResumeDto depuisEntite(CategorieUtilisateur c) {
        if (c == null) return null;
        return new CategorieUtilisateurResumeDto(c.getId(), c.getLibelle());
    }
}
