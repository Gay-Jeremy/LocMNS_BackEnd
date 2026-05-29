package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.StatutEmprunt;

public record StatutEmpruntResume(Integer id, String libelle, String code) {

    public static StatutEmpruntResume depuisEntite(StatutEmprunt statutEmprunt) {
        if(statutEmprunt == null) return null;
        return new StatutEmpruntResume(statutEmprunt.getId(), statutEmprunt.getLibelle(), statutEmprunt.getCode());
    }
}
