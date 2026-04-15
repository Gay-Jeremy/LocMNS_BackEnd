package com.locmns.locmns_backend.dto;

import com.locmns.locmns_backend.model.StatutEmprunt;

import java.time.LocalDate;

public record EmpruntDto(LocalDate dateDebut,
                         LocalDate dateFin,
                         int materielId,
                         int utilisateurId){

}
