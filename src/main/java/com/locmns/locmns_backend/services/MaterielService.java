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

    private final MaterielRepository materielRepository;
    private final ModeleRepository modeleRepository;
    private final StatutMaterielRepository statutMaterielRepository;
    private final SalleRepository salleDao;
    private final EmpruntRepository empruntRepository;

    public Optional<MaterielResponseDto> obtenirMaterielParId(Integer id) {
        return materielRepository.findById(id)
                .map(MaterielResponseDto::depuisEntite);
    }

    public List<MaterielResponseDto> obtenirTousLesMateriels() {
        return materielRepository.findAll().stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirMaterielsSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return materielRepository.findAll().stream()
                    .map(MaterielResponseDto::depuisEntite)
                    .toList();
        }
        return materielRepository.retourneListeMaterielEmpruntsEnCoursParEmail(utilisateur.getEmail()).stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirMaterielsDisponibles() {
        return materielRepository.retourneListeMaterielDisponible().stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirListeMaterielPanneSignalee() {
        return materielRepository.retourneListeMaterielPanneSignales().stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirListeMaterielPanneSignaleeSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return materielRepository.retourneListeMaterielPanneSignales().stream()
                    .map(MaterielResponseDto::depuisEntite)
                    .toList();
        }
        return materielRepository.retourneListeMaterielPanneSignalesParEmail(utilisateur.getEmail()).stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public List<MaterielResponseDto> obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(
            LocalDate dateDebut, LocalDate dateFin, int id) {
        return materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(dateDebut, dateFin, id).stream()
                .map(MaterielResponseDto::depuisEntite)
                .toList();
    }

    public void verifierDisponibilitePourProlongation(int empruntId, LocalDate nouvelleDateFin) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));
        LocalDate debutPeriodeExtension = emprunt.getDateFin().plusDays(1);
        boolean conflit = empruntRepository.existsConflitEmpruntSurMaterielPourPeriode(
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
        if (materielRepository.findByNumSerie(materielDto.numSerie()).isPresent()) {
            throw new MetierException("Un matériel avec le numéro de série '" + materielDto.numSerie() + "' existe déjà");
        }
        Modele modele = modeleRepository.findById(materielDto.modeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Le modèle avec l'id " + materielDto.modeleId() + " est introuvable"));
        StatutMateriel statutMateriel = null;
        if (materielDto.statutMaterielId() != null) {
            statutMateriel = statutMaterielRepository.findById(materielDto.statutMaterielId())
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
        return MaterielResponseDto.depuisEntite(materielRepository.save(materiel));
    }

    @Transactional
    public void modifierMateriel(Integer id, MaterielDto materielDto) {

        Materiel materiel = materielRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel non trouvé : " + id));

        Modele modele = modeleRepository.findById(materielDto.modeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Modele non trouvé :" + materielDto.modeleId()));

        StatutMateriel statutMateriel = null;
        if(materielDto.statutMaterielId() != null) {
            statutMateriel = statutMaterielRepository.findById(materielDto.statutMaterielId())
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
        materielRepository.save(materiel);
    }

    public void modifierStatutMateriel(Integer materielId, Integer statutId) {
        // On travaille avec l'entité Materiel, pas le DTO
        Materiel materiel = materielRepository.findById(materielId)
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel non trouvé : " + materielId));
        if (statutId == null) {
            materiel.setStatutMateriel(null);
        } else {
            StatutMateriel statutMateriel = statutMaterielRepository.findById(statutId)
                    .orElseThrow(() -> new RessourceIntrouvableException("Statut non trouvé : " + statutId));
            materiel.setStatutMateriel(statutMateriel);
        }
        materielRepository.save(materiel);
    }

}