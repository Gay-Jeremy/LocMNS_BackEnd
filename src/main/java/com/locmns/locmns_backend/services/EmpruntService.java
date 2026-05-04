package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dao.EmpruntDao;
import com.locmns.locmns_backend.dao.MaterielDao;
import com.locmns.locmns_backend.dao.StatutEmpruntDao;
import com.locmns.locmns_backend.dao.UtilisateurDao;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.model.StatutEmprunt;
import com.locmns.locmns_backend.model.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmpruntService {

    private  final EmpruntDao empruntDao;

    private  final MaterielDao materielDao;

    private  final UtilisateurDao utilisateurDao;

    private  final StatutEmpruntDao statutEmpruntDao;
    
    public Optional<Emprunt> obtenirEmpruntsParId(Integer id){ return empruntDao.findById(id); }
    
    public List<Emprunt> obtenirTousLesEmprunts(){ return empruntDao.findAll(); }

    public List<Emprunt> obtenirEmpruntsActifs(){ return empruntDao.retourneListeEmpruntActif(); }

    public List<Emprunt> obtenirDemandesEmprunt(){ return empruntDao.retourneListeDemandeEmprunt(); }

    public List<Emprunt> obtenirEmpruntsRecents() {
        return empruntDao.retourneEmpruntRecent(LocalDate.now().minusDays(30));
    }

    public List<Emprunt> obtenirListeEmpruntEnRetard () {
        return  empruntDao.retourneEmpruntEnRetard();
    }

    public List<Emprunt> obtenirHistoriqueEmprunt () {
        return  empruntDao.retourneHistoriqueEmprunt();
    }



    public void supprimerEmprunt(Integer id) {
        if (!empruntDao.existsById(id)) {
            throw new RessourceIntrouvableException("Emprunt introuvable");
        }
        empruntDao.deleteById(id);
    }

    public void modifierEmprunt(Integer id, Emprunt emprunt) {
        if (!empruntDao.existsById(id)) {
            throw new RessourceIntrouvableException("Emprunt introuvable");
        }
        emprunt.setId(id);
        empruntDao.save(emprunt);
    }

    public void modifierStatutEmprunt(int empruntId, int statutId) {
        Emprunt emprunt = empruntDao.findById(empruntId)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt non trouvé : " + empruntId));

        StatutEmprunt statutEmprunt = statutEmpruntDao.findById(statutId)
                .orElseThrow(() -> new RessourceIntrouvableException("Statut non trouvé : " + statutId));

        emprunt.setStatutEmprunt(statutEmprunt);
        empruntDao.save(emprunt);
    }


    public void creerEmprunt(EmpruntDto empruntDto) {

        // Vérification dateFin > dateDebut
        if (!empruntDto.dateFin().isAfter(empruntDto.dateDebut())) {
            throw new MetierException("La date de fin doit être après la date de début");
        }

        // Vérification dateDebut >= aujourd'hui
        if (empruntDto.dateDebut().isBefore(LocalDate.now())) {
            throw new MetierException("La date de début ne peut pas être dans le passé");
        }

        // Vérification dateFin >= aujourd'hui
        if (empruntDto.dateFin().isBefore(LocalDate.now())) {
            throw new MetierException("La date de fin ne peut pas être dans le passé");
        }

        // Vérification que le matériel existe
        Materiel materiel = materielDao.findById(empruntDto.materielId())
                .orElseThrow(() -> new MetierException("Matériel introuvable"));

        // Vérification que l'utilisateur existe
        Utilisateur utilisateur = utilisateurDao.findById(empruntDto.utilisateurId())
                .orElseThrow(() -> new MetierException("Utilisateur introuvable"));

        // Vérification disponibilité du matériel sur la période
        List<Materiel> disponibles = materielDao
                .retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                        empruntDto.dateDebut(), empruntDto.dateFin(), materiel.getModele().getCategorieModele().getId()
                );

        boolean materielDisponible = disponibles.stream()
                .anyMatch(m -> m.getId() == empruntDto.materielId());

        if (!materielDisponible) {
            throw new MetierException("Ce matériel n'est pas disponible sur cette période");
        }

        StatutEmprunt statutEmprunt = statutEmpruntDao.findById(1)
                .orElseThrow(() -> new MetierException("Statut emprunt introuvable"));

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setMateriel(materiel);
        emprunt.setDateDebut(empruntDto.dateDebut());
        emprunt.setDateFin(empruntDto.dateFin());
        emprunt.setStatutEmprunt(statutEmprunt);

        empruntDao.save(emprunt);
    }

}
