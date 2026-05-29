package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Materiel;

public record MaterielResume(Integer id, String numSerie, ModeleResume modele) {

    public static MaterielResume depuisEntite(Materiel materiel) {
        if (materiel == null) return null;
        return new MaterielResume(
                materiel.getId(),
                materiel.getNumSerie(),
                ModeleResume.depuisEntite(materiel.getModele())
        );
    }

}
