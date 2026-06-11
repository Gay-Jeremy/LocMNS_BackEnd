package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Materiel;

public record MaterielResumeDto(Integer id, String numSerie,
                                StatutMaterielResumeDto statutMateriel,
                                ModeleResumeDto modele) {

}
