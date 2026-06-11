package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Emprunt;

import java.time.LocalDate;

public record EmpruntResponseDto(
        Integer id,
        LocalDate dateCreation,
        LocalDate dateDebut,
        LocalDate dateFin,
        LocalDate dateProlongation,
        LocalDate dateRetourAnticipe,
        String note,
        UtilisateurResumeDto utilisateur,
        StatutEmpruntResumeDto statutEmprunt,
        MaterielResumeDto materiel
) {

    public static EmpruntResponseDto depuisEntite(Emprunt emprunt) {
        return new EmpruntResponseDto(
                emprunt.getId(),
                emprunt.getDateCreation(),
                emprunt.getDateDebut(),
                emprunt.getDateFin(),
                emprunt.getDateProlongation(),
                emprunt.getDateRetourAnticipe(),
                emprunt.getNote(),
                UtilisateurResumeDto.depuisEntite(emprunt.getUtilisateur()),
                StatutEmpruntResumeDto.depuisEntite(emprunt.getStatutEmprunt()),
                MaterielResumeDto.depuisEntite(emprunt.getMateriel())
        );
    }

}