package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.StatutEmprunt;

public record StatutEmpruntResumeDto(Integer id, String libelle, String code) {

    public static StatutEmpruntResumeDto depuisEntite(StatutEmprunt statutEmprunt) {
        if(statutEmprunt == null) return null;
        return new StatutEmpruntResumeDto(statutEmprunt.getId(), statutEmprunt.getLibelle(), statutEmprunt.getCode());
    }
}
