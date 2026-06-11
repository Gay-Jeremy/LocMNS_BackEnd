package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Modele;

public record ModeleResumeDto(Integer id, String libelle, CategorieModeleResumeDto categorieModele) {

    public static ModeleResumeDto depuisEntite(Modele modele) {
        if (modele == null) return null;
        return new ModeleResumeDto(
                modele.getId(),
                modele.getLibelle(),
                CategorieModeleResumeDto.depuisEntite(modele.getCategorieModele())
        );
    }

}