package com.locmns.locmns_backend.dto.response;

import java.time.LocalDate;

public record EmpruntResponseDto(
        Integer id,
        LocalDate dateCreation,
        LocalDate dateDebut,
        LocalDate dateFin,
        LocalDate dateRetourReel,
        LocalDate dateProlongation,
        LocalDate dateRetourAnticipe,
        String note,
        UtilisateurResumeDto utilisateur,
        StatutEmpruntResumeDto statutEmprunt,
        MaterielResumeDto materiel) {

}