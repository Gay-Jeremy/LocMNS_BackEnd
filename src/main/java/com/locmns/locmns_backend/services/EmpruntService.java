package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.constants.CodeStatutEmprunt;
import com.locmns.locmns_backend.constants.Role;
import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.ConflitReservationException;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.*;
import com.locmns.locmns_backend.validator.emprunt.EmpruntValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final IMaterielService materielService;
    private final EmpruntValidator empruntValidator;

    private List<String> statutsExclus() {
        return List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.REFUSE.name()
        );
    }

    // =========================================================
    // UTIL
    // =========================================================

    private Emprunt getEmprunt(Integer id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt non trouvé : " + id));
    }

    private StatutEmprunt getStatutParCode(CodeStatutEmprunt code) {
        return statutEmpruntRepository.findByCode(code.name())
                .orElseThrow(() -> new RessourceIntrouvableException("Statut introuvable : " + code));
    }

    private boolean isAdmin(Utilisateur user) {
        return Role.ADMIN.name().equals(user.getCategorieUtilisateur().getCode());
    }

    private List<EmpruntResponseDto> map(List<Emprunt> list) {
        return list.stream().map(EmpruntResponseDto::depuisEntite).toList();
    }

    private List<String> statutsDemandes() {
        return List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.DEMANDE_PROLONGATION.name(),
                CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()
        );
    }

    // =========================================================
    // READ
    // =========================================================

    @Override
    public Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id) {
        return empruntRepository.findById(id).map(EmpruntResponseDto::depuisEntite);
    }

    @Override
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return map(empruntRepository.findAll(Sort.by("dateCreation")));
    }


    @Override
    public List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur) {
        return isAdmin(utilisateur)
                ? map(empruntRepository.retourneListeEmpruntActif(statutsExclus()))
                : map(empruntRepository.retourneListeEmpruntActifParEmail(statutsExclus(), utilisateur.getEmail()));
    }


    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {
        return isAdmin(utilisateur)
                ? map(empruntRepository.retourneListeDemandeEmprunt(statutsDemandes()))
                : map(empruntRepository.retourneListeDemandeEmpruntParEmail(statutsDemandes(), utilisateur.getEmail()));
    }


    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {
        return isAdmin(utilisateur)
                ? map(empruntRepository.retourneEmpruntRecent(LocalDate.now().minusDays(7)))
                : map(empruntRepository.retourneEmpruntRecentParEmail(LocalDate.now().minusDays(7), utilisateur.getEmail()));
    }


    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {
        return isAdmin(utilisateur)
                ? map(empruntRepository.retourneEmpruntEnRetard())
                : map(empruntRepository.retourneEmpruntEnRetardParEmail(utilisateur.getEmail()));
    }


    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {
        return isAdmin(utilisateur)
                ? map(empruntRepository.retourneHistoriqueEmprunt())
                : map(empruntRepository.retourneHistoriqueEmpruntParEmail(utilisateur.getEmail()));
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    @Override
    public EmpruntResponseDto creerEmprunt(EmpruntRequestDto dto) {

        empruntValidator.validerCreationOuModification(dto);

        Materiel materiel = materielRepository.findById(dto.materielId())
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(dto.utilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Utilisateur introuvable"));

        List<Materiel> disponibles =
                materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                        dto.dateDebut(),
                        dto.dateFin(),
                        materiel.getModele().getCategorieModele().getId()
                );

        boolean disponible = disponibles.stream().anyMatch(m -> m.getId().equals(dto.materielId()));
        if (!disponible) {
            throw new ConflitReservationException("Matériel indisponible");
        }

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setMateriel(materiel);
        emprunt.setDateDebut(dto.dateDebut());
        emprunt.setDateFin(dto.dateFin());
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_EN_COURS));
        emprunt.setNote(dto.note());

        return EmpruntResponseDto.depuisEntite(empruntRepository.save(emprunt));
    }

    // =========================================================
    // UPDATE SIMPLE
    // =========================================================

    @Transactional
    @Override
    public void modifierEmprunt(Integer id, EmpruntRequestDto dto) {

        empruntValidator.validerCreationOuModification(dto);

        Emprunt emprunt = getEmprunt(id);

        Materiel materiel = materielRepository.findById(dto.materielId())
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(dto.utilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Utilisateur introuvable"));

        emprunt.setMateriel(materiel);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDateDebut(dto.dateDebut());
        emprunt.setDateFin(dto.dateFin());
        emprunt.setNote(dto.note());

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // CHANGEMENT DE STATUT
    // =========================================================

    @Transactional
    @Override
    public void modifierStatutEmprunt(int empruntId, StatutUpdateRequestDto dto) {

        Emprunt emprunt = getEmprunt(empruntId);

        CodeStatutEmprunt code;
        try {
            code = CodeStatutEmprunt.valueOf(dto.code());
        } catch (IllegalArgumentException e) {
            throw new RessourceIntrouvableException("Code statut inconnu : " + dto.code());
        }

        switch (code) {
            // statuts simples : pas de logique métier supplémentaire
            case ACCEPTE, REFUSE,
                 PROLONGATION_REFUSE, DEMANDE_RETOUR_ANTICIPE_REFUSE -> { }

            default -> throw new MetierException(
                    "Utilisez les endpoints dédiés pour le statut : " + code
            );
        }

        emprunt.setStatutEmprunt(getStatutParCode(code));
        empruntRepository.save(emprunt);
    }

    // =========================================================
    // DEMANDE PROLONGATION
    // =========================================================

    @Transactional
    @Override
    public void demanderProlongation(int id, DemandeProlongationRequestDto dto) {

        Emprunt emprunt = getEmprunt(id);

        if (dto.dateProlongation() == null) {
            throw new MetierException("dateProlongation obligatoire");
        }

        empruntValidator.validerProlongation(emprunt, dto.dateProlongation());

        emprunt.setDateProlongation(dto.dateProlongation());
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_PROLONGATION));

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // ACCEPTER PROLONGATION (admin)
    // =========================================================

    @Transactional
    @Override
    public void accepterProlongation(int id) {

        Emprunt emprunt = getEmprunt(id);

        if (emprunt.getDateProlongation() == null) {
            throw new MetierException("Aucune prolongation en attente");
        }

        emprunt.setDateFin(emprunt.getDateProlongation());
        emprunt.setDateProlongation(null);
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.PROLONGATION_ACCEPTE));

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // DEMANDE RETOUR ANTICIPÉ
    // =========================================================

    @Transactional
    @Override
    public void demanderRetourAnticipe(int id, DemandeRetourAnticipeRequestDto dto) {

        Emprunt emprunt = getEmprunt(id);

        if (dto.dateRetourAnticipe() == null) {
            throw new MetierException("dateRetourAnticipe obligatoire");
        }

        if (!dto.dateRetourAnticipe().isAfter(emprunt.getDateDebut())) {
            throw new MetierException("La date de retour anticipé doit être après la date de début");
        }

        if (!dto.dateRetourAnticipe().isBefore(emprunt.getDateFin())) {
            throw new MetierException("La date de retour anticipé doit être avant la date de fin");
        }

        emprunt.setDateRetourAnticipe(dto.dateRetourAnticipe());
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE));

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // ACCEPTER RETOUR ANTICIPÉ (admin)
    // =========================================================

    @Transactional
    @Override
    public void accepterRetourAnticipe(int id) {

        Emprunt emprunt = getEmprunt(id);

        if (emprunt.getDateRetourAnticipe() == null) {
            throw new MetierException("Aucun retour anticipé demandé");
        }

        emprunt.setDateFin(emprunt.getDateRetourAnticipe());
        emprunt.setDateRetourAnticipe(null);
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE_ACCEPTE));

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void supprimerEmprunt(Integer id) {
        if (!empruntRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Emprunt introuvable");
        }
        empruntRepository.deleteById(id);
    }
}