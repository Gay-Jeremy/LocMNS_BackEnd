package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Materiel;

public record MaterielResumeDto(Integer id, String numSerie, StatutMaterielResumeDto statutMateriel, ModeleResumeDto modele) {

    public static MaterielResumeDto depuisEntite(Materiel materiel) {
        if (materiel == null) return null;
        return new MaterielResumeDto(
                materiel.getId(),
                materiel.getNumSerie(),
                StatutMaterielResumeDto.depuisEntite(materiel.getStatutMateriel()),
                ModeleResumeDto.depuisEntite(materiel.getModele())
        );
    }

}
