package com.locmns.locmns_backend.validator.emprunt;

import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmpruntValidator {

    public void validerProlongation(Emprunt emprunt, LocalDate date) {

        if (date == null) {
            throw new MetierException("La date de prolongation est obligatoire");
        }

        if (!date.isAfter(emprunt.getDateFin())) {
            throw new MetierException("La date de prolongation doit être postérieure à la date de fin de l'emprunt");
        }
    }

    public void validerRetourAnticipe(Emprunt emprunt, LocalDate date) {

        if (date == null) {
            throw new MetierException("La date de retour anticipé est obligatoire");
        }

        if (!date.isBefore(emprunt.getDateFin())) {
            throw new MetierException("La date de retour anticipé doit être antérieure à la date de fin de l'emprunt");
        }
    }
}