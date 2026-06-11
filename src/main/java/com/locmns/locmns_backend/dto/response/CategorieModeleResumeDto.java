package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.CategorieModele;

public record CategorieModeleResumeDto(Integer id, String  libelle) {

    public static CategorieModeleResumeDto depuisEntite(CategorieModele categorieModele) {
        if(categorieModele == null) return null;
        return new CategorieModeleResumeDto(categorieModele.getId(), categorieModele.getLibelle());
    }
}
