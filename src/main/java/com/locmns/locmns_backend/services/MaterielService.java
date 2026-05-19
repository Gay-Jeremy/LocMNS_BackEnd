package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.MaterielDao;
import com.locmns.locmns_backend.repository.ModeleDao;
import com.locmns.locmns_backend.repository.SalleDao;
import com.locmns.locmns_backend.repository.StatutMaterielDao;
import com.locmns.locmns_backend.dto.MaterielDto;
import com.locmns.locmns_backend.exception.MetierException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterielService {

    private final MaterielDao materielDao;

    private final ModeleDao modeleDao;

    private final StatutMaterielDao statutMaterielDao;

    private final SalleDao salleDao;

    public Optional<Materiel> obtenirMaterielParId(Integer id) {
        return materielDao.findById(id);
    }

    public List<Materiel> obtenirTousLesMateriels() {
        return materielDao.findAll();
    }

    public List<Materiel> obtenirMaterielsDisponibles() { return materielDao.retourneListeMaterielDisponible(); }

    public List<Materiel> obtenirListeMaterielPanneSignalee() { return materielDao.retourneListeMaterielPanneSignales(); }

    public List<Materiel> obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(LocalDate dateDebut, LocalDate dateFin, int id) {
        return materielDao.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(dateDebut, dateFin, id);
    }

    public Materiel creerMateriel(MaterielDto materielDto, Utilisateur utilisateur) {


        if (materielDao.findByNumSerie(materielDto.numSerie()).isPresent()) {
            throw new MetierException("Un matériel avec le numéro de série '" + materielDto.numSerie() + "' existe déjà");
        }


        Modele modele = modeleDao.findById(materielDto.modeleId())
                .orElseThrow(() -> new MetierException("Le modèle avec l'id " + materielDto.modeleId() + " est introuvable"));


        StatutMateriel statutMateriel = null;
        if (materielDto.statutMaterielId() != null) {
            statutMateriel = statutMaterielDao.findById(materielDto.statutMaterielId())
                    .orElseThrow(() -> new MetierException("Le statut matériel avec l'id " + materielDto.statutMaterielId() + " est introuvable"));
        }


        Salle salle = null;
        if (materielDto.salleId() != null) {
            salle = salleDao.findById(materielDto.salleId())
                    .orElseThrow(() -> new MetierException("La salle avec l'id " + materielDto.salleId() + " est introuvable"));
        }

        Materiel materiel = new Materiel();
        materiel.setCreateur(utilisateur.getNom());
        materiel.setNumSerie(materielDto.numSerie());
        materiel.setModele(modele);
        materiel.setStatutMateriel(statutMateriel);
        materiel.setSalle(salle);

        return materielDao.save(materiel);
    }

    public void modifierStatutMateriel(Integer materielId, Integer statutId) {

        Materiel materiel = materielDao.findById(materielId)
                .orElseThrow(() -> new RessourceIntrouvableException("Materiel non trouvé : " + materielId));

        // null = statut "Disponible" → on efface le statut
        if (statutId == null) {
            materiel.setStatutMateriel(null);
        } else {
            StatutMateriel statutMateriel = statutMaterielDao.findById(statutId)
                    .orElseThrow(() -> new RessourceIntrouvableException("Statut non trouvé : " + statutId));
            materiel.setStatutMateriel(statutMateriel);
        }

        materielDao.save(materiel);
    }


}
