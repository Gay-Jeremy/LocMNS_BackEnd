package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.constants.CodeStatutEmprunt;
import com.locmns.locmns_backend.constants.Role;
import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.dto.request.StatutRequestDto;
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
    private final IMaterielService IMaterielService;

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
        String code = utilisateur.getCategorieUtilisateur().getCode();
        if (Role.ADMIN.name().equals(code)) {
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

        String code = utilisateur.getCategorieUtilisateur().getCode();
        if (Role.ADMIN.name().equals(code))  {
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
        String code = utilisateur.getCategorieUtilisateur().getCode();
        if (Role.ADMIN.name().equals(code)) {
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
        String code = utilisateur.getCategorieUtilisateur().getCode();
        if (Role.ADMIN.name().equals(code)) {
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
        String code = utilisateur.getCategorieUtilisateur().getCode();
        if (Role.ADMIN.name().equals(code)) {
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
    public void modifierEmprunt(Integer id, EmpruntRequestDto empruntRequestDto) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));

        Materiel materiel = materielRepository.findById(empruntRequestDto.materielId())
                .orElseThrow(() -> new MetierException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(empruntRequestDto.utilisateurId())
                .orElseThrow(() -> new MetierException("Utilisateur introuvable"));

        emprunt.setMateriel(materiel);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDateDebut(empruntRequestDto.dateDebut());
        emprunt.setDateFin(empruntRequestDto.dateFin());
        emprunt.setNote(empruntRequestDto.note());
        empruntRepository.save(emprunt);
    }

    @Transactional
    @Override
    public void modifierStatutEmpruntFromDto(int empruntId, StatutRequestDto statutRequestDto) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt non trouvé : " + empruntId));

        StatutEmprunt nouveauStatut = statutEmpruntRepository.findById(statutRequestDto.getStatutId())
                .orElseThrow(() -> new RessourceIntrouvableException("Statut non trouvé : " + statutRequestDto.getStatutId()));

        String code = statutRequestDto.getCode();
        if (code == null || code.isBlank()) {
            code = nouveauStatut.getCode();
        }

        LocalDate date = statutRequestDto.getDate();

        if (CodeStatutEmprunt.DEMANDE_PROLONGATION.name().equals(code)) {
            if (date == null) throw new MetierException("La date de prolongation est obligatoire");

            if (!date.isAfter(emprunt.getDateFin())) throw new MetierException("La date de prolongation doit être postérieure à la date de fin de l'emprunt");

            IMaterielService.verifierDisponibilitePourProlongation(empruntId, date);

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
    public EmpruntResponseDto creerEmprunt(EmpruntRequestDto empruntRequestDto) {
        if (!empruntRequestDto.dateFin().isAfter(empruntRequestDto.dateDebut()))
            throw new MetierException("La date de fin doit être après la date de début");

        if (empruntRequestDto.dateDebut().isBefore(LocalDate.now()))
            throw new MetierException("La date de début ne peut pas être dans le passé");

        if (empruntRequestDto.dateFin().isBefore(LocalDate.now()))
            throw new MetierException("La date de fin ne peut pas être dans le passé");

        Materiel materiel = materielRepository.findById(empruntRequestDto.materielId())
                .orElseThrow(() -> new MetierException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(empruntRequestDto.utilisateurId())
                .orElseThrow(() -> new MetierException("Utilisateur introuvable"));

        List<Materiel> disponibles = materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                empruntRequestDto.dateDebut(), empruntRequestDto.dateFin(), materiel.getModele().getCategorieModele().getId()
        );

        boolean materielDisponible = disponibles.stream()
                .anyMatch(m -> m.getId().equals(empruntRequestDto.materielId()));

        if (!materielDisponible)
            throw new MetierException("Ce matériel n'est pas disponible sur cette période");

        StatutEmprunt statutEmprunt = statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_EN_COURS.name())
                .orElseThrow(() -> new MetierException("Statut emprunt introuvable"));

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setMateriel(materiel);
        emprunt.setDateDebut(empruntRequestDto.dateDebut());
        emprunt.setDateFin(empruntRequestDto.dateFin());
        emprunt.setStatutEmprunt(statutEmprunt);
        emprunt.setNote(empruntRequestDto.note());

        return EmpruntResponseDto.depuisEntite(empruntRepository.save(emprunt));
    }

}