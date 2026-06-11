package com.locmns.locmns_backend.dto.response;

public record MaterielResponseDto(Integer id,
                                  String numSerie,
                                  SalleResumeDto salle,
                                  StatutMaterielResumeDto statutMateriel,
                                  ModeleResumeDto modele) {

}
