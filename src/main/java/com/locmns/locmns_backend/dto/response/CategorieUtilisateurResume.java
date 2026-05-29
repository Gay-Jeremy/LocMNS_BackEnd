package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.CategorieUtilisateur;
import com.locmns.locmns_backend.model.Promotion;

public record CategorieUtilisateurResume(Integer id, String libelle) {

    public static CategorieUtilisateurResume depuisEntite(CategorieUtilisateur c) {
        if (c == null) return null;
        return new CategorieUtilisateurResume(c.getId(), c.getLibelle());
    }
}
