package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Modele;

public record ModeleResume(Integer id, String libelle, CategorieModeleResume categorieModele) {

    public static ModeleResume depuisEntite(Modele modele) {
        if (modele == null) return null;
        return new ModeleResume(
                modele.getId(),
                modele.getLibelle(),
                CategorieModeleResume.depuisEntite(modele.getCategorieModele())
        );
    }

}