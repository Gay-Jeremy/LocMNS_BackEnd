package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Materiel;

public record MaterielResponseDto(Integer id, String numSerie, SalleResumeDto salle, StatutMaterielResumeDto statutMateriel, ModeleResumeDto modele) {

    public static MaterielResponseDto depuisEntite(Materiel materiel) {
        if(materiel == null) return null;

        return new MaterielResponseDto(
                materiel.getId(),
                materiel.getNumSerie(),
                SalleResumeDto.depuisEntite(materiel.getSalle()),
                StatutMaterielResumeDto.depuisEntite(materiel.getStatutMateriel()),
                ModeleResumeDto.depuisEntite(materiel.getModele())
        );
    }
}
