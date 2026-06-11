package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Modele;

public record ModeleResumeDto(Integer id,
                              String libelle,
                              CategorieModeleResumeDto categorieModele) {

}