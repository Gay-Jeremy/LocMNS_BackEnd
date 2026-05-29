package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.model.Salle;

public record MaterielResponseDto(Integer id, String numSerie, SalleResume salle,StatutMaterielResume statutMateriel, ModeleResume modele) {

    public static MaterielResponseDto depuisEntite(Materiel materiel) {
        if(materiel == null) return null;

        return new MaterielResponseDto(
                materiel.getId(),
                materiel.getNumSerie(),
                SalleResume.depuisEntite(materiel.getSalle()),
                StatutMaterielResume.depuisEntite(materiel.getStatutMateriel()),
                ModeleResume.depuisEntite(materiel.getModele())
        );
    }
}
