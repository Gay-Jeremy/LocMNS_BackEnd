package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.MaterielDto;
import com.locmns.locmns_backend.dto.response.MaterielResponseDto;
import com.locmns.locmns_backend.exception.ConflitReservationException;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EmpruntDao empruntDao;

    public Optional<MaterielResponseDto> obtenirMaterielParId(Integer id) {
        return materielDao.findById(id)
                .map(MaterielResponseDto::depuisEntite);
    }

    public List<MaterielResponseDto> obtenirTousLesMateriels() {
        return materielDao.findAll().stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirMaterielsSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return materielDao.findAll().stream()
                    .map(MaterielResponseDto::depuisEntite)
                    .toList();
        }
        return materielDao.retourneListeMaterielEmpruntsEnCoursParEmail(utilisateur.getEmail()).stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirMaterielsDisponibles() {
        return materielDao.retourneListeMaterielDisponible().stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirListeMaterielPanneSignalee() {
        return materielDao.retourneListeMaterielPanneSignales().stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirListeMaterielPanneSignaleeSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return materielDao.retourneListeMaterielPanneSignales().stream()
                    .map(MaterielResponseDto::depuisEntite)
                    .toList();
        }
        return materielDao.retourneListeMaterielPanneSignalesParEmail(utilisateur.getEmail()).stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(
            LocalDate dateDebut, LocalDate dateFin, int id) {
        return materielDao.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(dateDebut, dateFin, id).stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public void verifierDisponibilitePourProlongation(int empruntId, LocalDate nouvelleDateFin) {
        Emprunt emprunt = empruntDao.findById(empruntId)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));
        LocalDate debutPeriodeExtension = emprunt.getDateFin().plusDays(1);
        boolean conflit = empruntDao.existsConflitEmpruntSurMaterielPourPeriode(
                emprunt.getMateriel().getId(),
                empruntId,
                debutPeriodeExtension,
                nouvelleDateFin
        );
        if (conflit) {
            throw new ConflitReservationException(
                    "Le matériel n'est pas disponible pour cette période : un autre emprunt ou une demande existe déjà sur ces dates"
            );
        }
    }

    public MaterielResponseDto creerMateriel(MaterielDto materielDto, Utilisateur utilisateur) {
        if (materielDao.findByNumSerie(materielDto.numSerie()).isPresent()) {
            throw new MetierException("Un matériel avec le numéro de série '" + materielDto.numSerie() + "' existe déjà");
        }
        Modele modele = modeleDao.findById(materielDto.modeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Le modèle avec l'id " + materielDto.modeleId() + " est introuvable"));
        StatutMateriel statutMateriel = null;
        if (materielDto.statutMaterielId() != null) {
            statutMateriel = statutMaterielDao.findById(materielDto.statutMaterielId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Le statut matériel avec l'id " + materielDto.statutMaterielId() + " est introuvable"));
        }
        Salle salle = null;
        if (materielDto.salleId() != null) {
            salle = salleDao.findById(materielDto.salleId())
                    .orElseThrow(() -> new RessourceIntrouvableException("La salle avec l'id " + materielDto.salleId() + " est introuvable"));
        }

        Materiel materiel = new Materiel();
        materiel.setNumSerie(materielDto.numSerie());
        materiel.setModele(modele);
        materiel.setStatutMateriel(statutMateriel);
        materiel.setSalle(salle);
        return MaterielResponseDto.depuisEntite(materielDao.save(materiel));
    }

    @Transactional
    public void modifierMateriel(Integer id, MaterielDto materielDto) {

        Materiel materiel = materielDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel non trouvé : " + id));

        Modele modele = modeleDao.findById(materielDto.modeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Modele non trouvé :" + materielDto.modeleId()));

        StatutMateriel statutMateriel = null;
        if(materielDto.statutMaterielId() != null) {
            statutMateriel = statutMaterielDao.findById(materielDto.statutMaterielId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Le statut matériel avec l'id " + materielDto.statutMaterielId() + " est introuvable"));
        }
        Salle salle = null;
        if(materielDto.salleId() != null) {
            salle = salleDao.findById(materielDto.salleId())
                    .orElseThrow(() -> new RessourceIntrouvableException("La salle avec l'id " + materielDto.salleId() + " est introuvable"));
        }

        materiel.setModele(modele);
        materiel.setStatutMateriel(statutMateriel);
        materiel.setSalle(salle);
        materielDao.save(materiel);
    }

    public void modifierStatutMateriel(Integer materielId, Integer statutId) {
        // On travaille avec l'entité Materiel, pas le DTO
        Materiel materiel = materielDao.findById(materielId)
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel non trouvé : " + materielId));
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