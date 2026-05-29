package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.StatutMateriel;

public record StatutMaterielResume(Integer id, String libelle, String code) {

    public static StatutMaterielResume depuisEntite(StatutMateriel statutMateriel) {
        if(statutMateriel == null) return null ;
        return new StatutMaterielResume(
                statutMateriel.getId(),
                statutMateriel.getLibelle(),
                statutMateriel.getCode()
        );
    }
}
