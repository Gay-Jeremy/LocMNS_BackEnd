package com.locmns.locmns_backend.validator.emprunt;

import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmpruntValidator {

    public void validerCreationOuModification(EmpruntRequestDto dto) {

        if (dto.dateDebut() == null) {
            throw new MetierException("La date de début est obligatoire");
        }

        if (dto.dateFin() == null) {
            throw new MetierException("La date de fin est obligatoire");
        }

        if (dto.dateDebut().isBefore(LocalDate.now())) {
            throw new MetierException("La date de début ne peut pas être dans le passé");
        }

        if (!dto.dateFin().isAfter(dto.dateDebut())) {
            throw new MetierException("La date de fin doit être après la date de début");
        }

        if (dto.materielId() == null) {
            throw new MetierException("Le matériel est obligatoire");
        }

        if (dto.utilisateurId() == null) {
            throw new MetierException("L'utilisateur est obligatoire");
        }
    }

    public void validerProlongation(Emprunt emprunt, LocalDate date) {

        if (date == null) {
            throw new MetierException("La date de prolongation est obligatoire");
        }

        if (!date.isAfter(emprunt.getDateFin())) {
            throw new MetierException(
                    "La date de prolongation doit être postérieure à la date de fin de l'emprunt"
            );
        }
    }

    public void validerRetourAnticipe(Emprunt emprunt, LocalDate date) {

        if (date == null) {
            throw new MetierException("La date de retour anticipé est obligatoire");
        }

        if (!date.isAfter(emprunt.getDateDebut())) {
            throw new MetierException(
                    "La date de retour anticipé doit être après la date de début"
            );
        }

        if (!date.isBefore(emprunt.getDateFin())) {
            throw new MetierException(
                    "La date de retour anticipé doit être antérieure à la date de fin de l'emprunt"
            );
        }
    }
}