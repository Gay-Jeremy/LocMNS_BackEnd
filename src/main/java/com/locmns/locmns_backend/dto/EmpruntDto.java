package com.locmns.locmns_backend.dto;

import java.time.LocalDate;

public record EmpruntDto(LocalDate dateDebut,
                         LocalDate dateFin,
                         int materielId,
                         int utilisateurId){

}
