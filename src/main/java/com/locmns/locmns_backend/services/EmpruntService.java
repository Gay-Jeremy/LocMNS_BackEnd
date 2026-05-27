package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.constants.CodeStatutEmprunt;
import com.locmns.locmns_backend.repository.EmpruntDao;
import com.locmns.locmns_backend.repository.MaterielDao;
import com.locmns.locmns_backend.repository.StatutEmpruntDao;
import com.locmns.locmns_backend.repository.UtilisateurDao;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.model.StatutEmprunt;
import com.locmns.locmns_backend.model.Utilisateur;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmpruntService implements IEmpruntService {

    private  final EmpruntDao empruntDao;

    private  final MaterielDao materielDao;

    private  final UtilisateurDao utilisateurDao;

    private  final StatutEmpruntDao statutEmpruntDao;

    private final MaterielService materielService;

    @Override
    public Optional<Emprunt> obtenirEmpruntsParId(Integer id){ return empruntDao.findById(id); }

    @Override
    public List<Emprunt> obtenirTousLesEmprunts(){ return empruntDao.findAll(); }

    @Override
    public List<Emprunt> obtenirEmpruntsActifs(){ return empruntDao.retourneListeEmpruntActif(CodeStatutEmprunt.ACCEPTE.name()); }

    @Override
    public List<Emprunt> obtenirEmpruntSelonRole(Utilisateur utilisateur) {

        String role = utilisateur.getCategorieUtilisateur().getLibelle();

        if (role.equals("Administrateur")) {
            return empruntDao.retourneListeEmpruntActif(CodeStatutEmprunt.ACCEPTE.name());
        }

        return empruntDao.retourneListeEmpruntActifParEmail(CodeStatutEmprunt.ACCEPTE.name(), utilisateur.getEmail());
    }

    @Override
    public List<Emprunt> obtenirDemandesEmprunt(){

        List<String> codes = List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.DEMANDE_PROLONGATION.name(),
                CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()
        );

        return empruntDao.retourneListeDemandeEmprunt(codes); }

    @Override
    public List<Emprunt> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {

        String role = utilisateur.getCategorieUtilisateur().getLibelle();

        List<String> codes = List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.DEMANDE_PROLONGATION.name(),
                CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()
        );


        if (role.equals("Administrateur")) {
            return empruntDao.retourneListeDemandeEmprunt(codes);
        }

        return empruntDao.retourneListeDemandeEmpruntParEmail(codes, utilisateur.getEmail());
    }


    @Override
    public List<Emprunt> obtenirEmpruntsRecents() {
        return empruntDao.retourneEmpruntRecent(LocalDate.now().minusDays(30));
    }

    @Override
    public List<Emprunt> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {

        String role = utilisateur.getCategorieUtilisateur().getLibelle();

        if(role.equals("Administrateur")) {
            return empruntDao.retourneEmpruntRecent(LocalDate.now().minusDays(30));
        }

        return empruntDao.retourneEmpruntRecentParEmail(LocalDate.now().minusDays(30), utilisateur.getEmail());
    }

    @Override
    public List<Emprunt> obtenirListeEmpruntEnRetard() {
        return  empruntDao.retourneEmpruntEnRetard();
    }

    @Override
    public List<Emprunt> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {

        String role = utilisateur.getCategorieUtilisateur().getLibelle();

        if(role.equals("Administrateur")) {
            return  empruntDao.retourneEmpruntEnRetard();
        }

        return empruntDao.retourneEmpruntEnRetardParEmail(utilisateur.getEmail());
    }

    @Override
    public List<Emprunt> obtenirHistoriqueEmprunt() {
        return  empruntDao.retourneHistoriqueEmprunt();
    }

    @Override
    public List<Emprunt> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {

        String role = utilisateur.getCategorieUtilisateur().getLibelle();

        if(role.equals("Administrateur")) {
            return  empruntDao.retourneHistoriqueEmprunt();
        }

        return empruntDao.retourneHistoriqueEmpruntParEmail(utilisateur.getEmail());
    }



    @Override
    public void supprimerEmprunt(Integer id) {
        if (!empruntDao.existsById(id)) {
            throw new RessourceIntrouvableException("Emprunt introuvable");
        }
        empruntDao.deleteById(id);
    }

    @Transactional
    @Override
    public void modifierEmprunt(Integer id, EmpruntDto empruntDto) {

        Emprunt emprunt = empruntDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));

        Materiel materiel = materielDao.findById(empruntDto.materielId())
                .orElseThrow(() -> new MetierException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurDao.findById(empruntDto.utilisateurId())
                .orElseThrow(() -> new MetierException("Utilisateur introuvable"));

        emprunt.setMateriel(materiel);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDateDebut(empruntDto.dateDebut());
        emprunt.setDateFin(empruntDto.dateFin());
        emprunt.setNote(empruntDto.note());

        empruntDao.save(emprunt);
    }

    @Transactional
    @Override
    public void modifierStatutEmpruntFromDto(int empruntId, StatutDto statutDto) {
        Emprunt emprunt = empruntDao.findById(empruntId)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt non trouvé : " + empruntId));

        StatutEmprunt nouveauStatut = statutEmpruntDao.findById(statutDto.getStatutId())
                .orElseThrow(() -> new RessourceIntrouvableException("Statut non trouvé : " + statutDto.getStatutId()));

        String code = statutDto.getCode();
        if (code == null || code.isBlank()) {
            code = nouveauStatut.getCode();
        }

        LocalDate date = statutDto.getDate();

        if (CodeStatutEmprunt.DEMANDE_PROLONGATION.name().equals(code)) {
            if (date == null) {
                throw new MetierException("La date de prolongation est obligatoire");
            }
            if (!date.isAfter(emprunt.getDateFin())) {
                throw new MetierException("La date de prolongation doit être postérieure à la date de fin de l'emprunt");
            }
            materielService.verifierDisponibilitePourProlongation(empruntId, date);
        } else if (CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name().equals(code)) {
            if (date == null) {
                throw new MetierException("La date de retour anticipé est obligatoire");
            }
            if (!date.isBefore(emprunt.getDateFin())) {
                throw new MetierException("La date de retour anticipé doit être antérieure à la date de fin de l'emprunt");
            }
        }

        emprunt.setStatutEmprunt(nouveauStatut);

        if (date != null) {
            if (CodeStatutEmprunt.DEMANDE_PROLONGATION.name().equals(code)) {
                emprunt.setDateProlongation(date);
            } else if (CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name().equals(code)) {
                emprunt.setDateRetourAnticipe(date);
            }
        }

        empruntDao.save(emprunt);
    }

    @Transactional
    @Override
    public Emprunt creerEmprunt(EmpruntDto empruntDto) {

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
                .anyMatch(m -> m.getId().equals(empruntDto.materielId()));

        if (!materielDisponible) {
            throw new MetierException("Ce matériel n'est pas disponible sur cette période");
        }

        StatutEmprunt statutEmprunt = statutEmpruntDao.findByCode(CodeStatutEmprunt.DEMANDE_EN_COURS.name())
                .orElseThrow(() -> new MetierException("Statut emprunt introuvable"));

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setMateriel(materiel);
        emprunt.setDateDebut(empruntDto.dateDebut());
        emprunt.setDateFin(empruntDto.dateFin());
        emprunt.setStatutEmprunt(statutEmprunt);
        emprunt.setNote(empruntDto.note());

        return empruntDao.save(emprunt);
    }

}
