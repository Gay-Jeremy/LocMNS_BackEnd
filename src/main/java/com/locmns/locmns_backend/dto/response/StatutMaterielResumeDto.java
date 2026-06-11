package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.StatutMateriel;

public record StatutMaterielResumeDto(Integer id, String libelle, String code) {

    public static StatutMaterielResumeDto depuisEntite(StatutMateriel statutMateriel) {
        if(statutMateriel == null) return null ;
        return new StatutMaterielResumeDto(
                statutMateriel.getId(),
                statutMateriel.getLibelle(),
                statutMateriel.getCode()
        );
    }
}
