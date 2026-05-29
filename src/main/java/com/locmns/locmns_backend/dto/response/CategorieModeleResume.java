package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.CategorieModele;

public record CategorieModeleResume(Integer id, String  libelle) {

    public static CategorieModeleResume depuisEntite(CategorieModele categorieModele) {
        if(categorieModele == null) return null;
        return new CategorieModeleResume(categorieModele.getId(), categorieModele.getLibelle());
    }
}
