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
        UtilisateurResume utilisateur,
        StatutEmpruntResume statutEmprunt,
        MaterielResume materiel
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
                UtilisateurResume.depuisEntite(emprunt.getUtilisateur()),
                StatutEmpruntResume.depuisEntite(emprunt.getStatutEmprunt()),
                MaterielResume.depuisEntite(emprunt.getMateriel())
        );
    }

}