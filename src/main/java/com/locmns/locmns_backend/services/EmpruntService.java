package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.constants.CodeStatutEmprunt;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
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
public class EmpruntService implements IEmpruntService {

    private final EmpruntRepository empruntRepository;
    private final MaterielRepository materielRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final StatutEmpruntRepository statutEmpruntRepository;
    private final MaterielService materielService;

    @Override
    public Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id) {
        return empruntRepository.findById(id).map(EmpruntResponseDto::depuisEntite);
    }

    @Override
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return empruntRepository.findAll().stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsActifs() {
        return empruntRepository.retourneListeEmpruntActif(CodeStatutEmprunt.ACCEPTE.name()).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return empruntRepository.retourneListeEmpruntActif(CodeStatutEmprunt.ACCEPTE.name()).stream()
                    .map(EmpruntResponseDto::depuisEntite)
                    .toList();
        }
        return empruntRepository.retourneListeEmpruntActifParEmail(CodeStatutEmprunt.ACCEPTE.name(), utilisateur.getEmail()).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmprunt() {
        List<String> codes = List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.DEMANDE_PROLONGATION.name(),
                CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()
        );
        return empruntRepository.retourneListeDemandeEmprunt(codes).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        List<String> codes = List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.DEMANDE_PROLONGATION.name(),
                CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()
        );
        if (role.equals("Administrateur")) {
            return empruntRepository.retourneListeDemandeEmprunt(codes).stream()
                    .map(EmpruntResponseDto::depuisEntite)
                    .toList();
        }
        return empruntRepository.retourneListeDemandeEmpruntParEmail(codes, utilisateur.getEmail()).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecents() {
        return empruntRepository.retourneEmpruntRecent(LocalDate.now().minusDays(30)).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return empruntRepository.retourneEmpruntRecent(LocalDate.now().minusDays(30)).stream()
                    .map(EmpruntResponseDto::depuisEntite)
                    .toList();
        }
        return empruntRepository.retourneEmpruntRecentParEmail(LocalDate.now().minusDays(30), utilisateur.getEmail()).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetard() {
        return empruntRepository.retourneEmpruntEnRetard().stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return empruntRepository.retourneEmpruntEnRetard().stream()
                    .map(EmpruntResponseDto::depuisEntite)
                    .toList();
        }
        return empruntRepository.retourneEmpruntEnRetardParEmail(utilisateur.getEmail()).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmprunt() {
        return empruntRepository.retourneHistoriqueEmprunt().stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {
        String role = utilisateur.getCategorieUtilisateur().getLibelle();
        if (role.equals("Administrateur")) {
            return empruntRepository.retourneHistoriqueEmprunt().stream()
                    .map(EmpruntResponseDto::depuisEntite)
                    .toList();
        }
        return empruntRepository.retourneHistoriqueEmpruntParEmail(utilisateur.getEmail()).stream()
                .map(EmpruntResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public void supprimerEmprunt(Integer id) {
        if (!empruntRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Emprunt introuvable");
        }
        empruntRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void modifierEmprunt(Integer id, EmpruntDto empruntDto) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));

        Materiel materiel = materielRepository.findById(empruntDto.materielId())
                .orElseThrow(() -> new MetierException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(empruntDto.utilisateurId())
                .orElseThrow(() -> new MetierException("Utilisateur introuvable"));

        emprunt.setMateriel(materiel);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDateDebut(empruntDto.dateDebut());
        emprunt.setDateFin(empruntDto.dateFin());
        emprunt.setNote(empruntDto.note());
        empruntRepository.save(emprunt);
    }

    @Transactional
    @Override
    public void modifierStatutEmpruntFromDto(int empruntId, StatutDto statutDto) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt non trouvé : " + empruntId));

        StatutEmprunt nouveauStatut = statutEmpruntRepository.findById(statutDto.getStatutId())
                .orElseThrow(() -> new RessourceIntrouvableException("Statut non trouvé : " + statutDto.getStatutId()));

        String code = statutDto.getCode();
        if (code == null || code.isBlank()) {
            code = nouveauStatut.getCode();
        }

        LocalDate date = statutDto.getDate();

        if (CodeStatutEmprunt.DEMANDE_PROLONGATION.name().equals(code)) {
            if (date == null) throw new MetierException("La date de prolongation est obligatoire");

            if (!date.isAfter(emprunt.getDateFin())) throw new MetierException("La date de prolongation doit être postérieure à la date de fin de l'emprunt");

            materielService.verifierDisponibilitePourProlongation(empruntId, date);

        } else if (CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name().equals(code)) {
            if (date == null) throw new MetierException("La date de retour anticipé est obligatoire");

            if (!date.isBefore(emprunt.getDateFin())) throw new MetierException("La date de retour anticipé doit être antérieure à la date de fin de l'emprunt");
        }
        emprunt.setStatutEmprunt(nouveauStatut);
        if (date != null) {
            if (CodeStatutEmprunt.DEMANDE_PROLONGATION.name().equals(code)) {
                emprunt.setDateProlongation(date);

            } else if (CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name().equals(code)) {
                emprunt.setDateRetourAnticipe(date);
            }
        }
        empruntRepository.save(emprunt);
    }

    @Transactional
    @Override
    public EmpruntResponseDto creerEmprunt(EmpruntDto empruntDto) {
        if (!empruntDto.dateFin().isAfter(empruntDto.dateDebut()))
            throw new MetierException("La date de fin doit être après la date de début");

        if (empruntDto.dateDebut().isBefore(LocalDate.now()))
            throw new MetierException("La date de début ne peut pas être dans le passé");

        if (empruntDto.dateFin().isBefore(LocalDate.now()))
            throw new MetierException("La date de fin ne peut pas être dans le passé");

        Materiel materiel = materielRepository.findById(empruntDto.materielId())
                .orElseThrow(() -> new MetierException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(empruntDto.utilisateurId())
                .orElseThrow(() -> new MetierException("Utilisateur introuvable"));

        List<Materiel> disponibles = materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                empruntDto.dateDebut(), empruntDto.dateFin(), materiel.getModele().getCategorieModele().getId()
        );

        boolean materielDisponible = disponibles.stream()
                .anyMatch(m -> m.getId().equals(empruntDto.materielId()));

        if (!materielDisponible)
            throw new MetierException("Ce matériel n'est pas disponible sur cette période");

        StatutEmprunt statutEmprunt = statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_EN_COURS.name())
                .orElseThrow(() -> new MetierException("Statut emprunt introuvable"));

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setMateriel(materiel);
        emprunt.setDateDebut(empruntDto.dateDebut());
        emprunt.setDateFin(empruntDto.dateFin());
        emprunt.setStatutEmprunt(statutEmprunt);
        emprunt.setNote(empruntDto.note());

        return EmpruntResponseDto.depuisEntite(empruntRepository.save(emprunt));
    }

}