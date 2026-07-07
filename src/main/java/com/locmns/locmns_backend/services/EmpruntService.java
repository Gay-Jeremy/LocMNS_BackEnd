package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.constants.CodeStatutEmprunt;
import com.locmns.locmns_backend.constants.Role;
import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.ConflitReservationException;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.EmpruntMapper;
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

/**
 * Service métier pour la gestion des emprunts.
 *
 * <p>Responsabilités :
 * <ol>
 *   <li>Valider les données entrantes via {@link EmpruntValidator}</li>
 *   <li>Résoudre les associations JPA (matériel, utilisateur, statut)</li>
 *   <li>Appliquer les règles métier (disponibilité, transitions de statut)</li>
 *   <li>Déléguer la conversion entité ↔ DTO à {@link EmpruntMapper}</li>
 * </ol>
 *
 * <p>Le mapper est le seul point de contact avec la couche DTO :
 * ce service ne construit jamais de DTO directement.
 */
@Service
@RequiredArgsConstructor
public class EmpruntService implements IEmpruntService {

    private final EmpruntRepository empruntRepository;
    private final MaterielRepository materielRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final StatutEmpruntRepository statutEmpruntRepository;
    private final EmpruntValidator empruntValidator;
    private final EmpruntMapper empruntMapper;

    // =========================================================
    // Méthodes utilitaires privées
    // =========================================================

    /**
     * Codes de statut exclus des listes "emprunts actifs"
     * (demandes non encore acceptées et demandes refusées).
     */
    private List<String> statutsExclus() {
        return List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.REFUSE.name()
        );
    }

    /**
     * Codes de statut correspondant à des demandes en attente de traitement.
     */
    private List<String> statutsDemandes() {
        return List.of(
                CodeStatutEmprunt.DEMANDE_EN_COURS.name(),
                CodeStatutEmprunt.DEMANDE_PROLONGATION.name(),
                CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()
        );
    }

    /**
     * Charge un emprunt par son identifiant ou lève une exception.
     *
     * @param id identifiant de l'emprunt
     * @return l'emprunt trouvé
     * @throws RessourceIntrouvableException si aucun emprunt ne correspond à {@code id}
     */
    private Emprunt getEmpruntOuException(Integer id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt non trouvé : " + id));
    }

    /**
     * Charge un statut d'emprunt par son code ou lève une exception.
     *
     * @param code le code fonctionnel du statut
     * @return le statut correspondant
     * @throws RessourceIntrouvableException si le code est introuvable en base
     */
    private StatutEmprunt getStatutParCode(CodeStatutEmprunt code) {
        return statutEmpruntRepository.findByCode(code.name())
                .orElseThrow(() -> new RessourceIntrouvableException("Statut introuvable : " + code));
    }

    /**
     * Indique si l'utilisateur possède le rôle administrateur.
     *
     * @param utilisateur l'utilisateur à tester
     * @return {@code true} si l'utilisateur est admin
     */
    private boolean isAdmin(Utilisateur utilisateur) {
        return Role.ADMIN.name().equals(utilisateur.getCategorieUtilisateur().getCode());
    }

    // =========================================================
    // Lecture
    // =========================================================

    @Override
    public Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id) {
        return empruntRepository.findById(id).map(empruntMapper::toDto);
    }

    @Override
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return empruntMapper.toDtoList(
                empruntRepository.findAll(Sort.by("dateCreation"))
        );
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur) {
        List<Emprunt> emprunts = isAdmin(utilisateur)
                ? empruntRepository.retourneListeEmpruntActif(statutsExclus())
                : empruntRepository.retourneListeEmpruntActifParEmail(statutsExclus(), utilisateur.getEmail());
        return empruntMapper.toDtoList(emprunts);
    }

    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {
        List<Emprunt> demandes = isAdmin(utilisateur)
                ? empruntRepository.retourneListeDemandeEmprunt(statutsDemandes())
                : empruntRepository.retourneListeDemandeEmpruntParEmail(statutsDemandes(), utilisateur.getEmail());
        return empruntMapper.toDtoList(demandes);
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {
        LocalDate seuilRecent = LocalDate.now().minusDays(7);
        List<Emprunt> recents = isAdmin(utilisateur)
                ? empruntRepository.retourneEmpruntRecent(seuilRecent)
                : empruntRepository.retourneEmpruntRecentParEmail(seuilRecent, utilisateur.getEmail());
        return empruntMapper.toDtoList(recents);
    }

    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {
        List<Emprunt> enRetard = isAdmin(utilisateur)
                ? empruntRepository.retourneEmpruntEnRetard()
                : empruntRepository.retourneEmpruntEnRetardParEmail(utilisateur.getEmail());
        return empruntMapper.toDtoList(enRetard);
    }

    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {
        List<Emprunt> historique = isAdmin(utilisateur)
                ? empruntRepository.retourneHistoriqueEmprunt()
                : empruntRepository.retourneHistoriqueEmpruntParEmail(utilisateur.getEmail());
        return empruntMapper.toDtoList(historique);
    }

    // =========================================================
    // Création
    // =========================================================

    /**
     * Crée un nouvel emprunt avec le statut initial {@code DEMANDE_EN_COURS}.
     *
     * <p>Étapes :
     * <ol>
     *   <li>Validation des dates et des identifiants via {@link EmpruntValidator}</li>
     *   <li>Résolution des associations (matériel, utilisateur)</li>
     *   <li>Vérification de la disponibilité du matériel sur la période demandée</li>
     *   <li>Construction de l'entité via {@link EmpruntMapper#toEntity}</li>
     *   <li>Affectation des associations et du statut initial</li>
     *   <li>Persistance et conversion en DTO</li>
     * </ol>
     *
     * @param dto les données de la demande d'emprunt
     * @return le DTO de l'emprunt créé
     * @throws RessourceIntrouvableException si le matériel ou l'utilisateur est introuvable,
     * @throws ConflitReservationException si le matériel est indisponible sur la période
     */
    @Transactional
    @Override
    public EmpruntResponseDto creerEmprunt(EmpruntRequestDto dto, Utilisateur connecte) {

        empruntValidator.validerCreationOuModification(dto);

        Utilisateur utilisateur;
        if (isAdmin(connecte)) {
            if (dto.utilisateurId() == null) {
                throw new MetierException("Un admin doit spécifier l'utilisateur cible");
            }
            utilisateur = utilisateurRepository.findById(dto.utilisateurId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Utilisateur introuvable"));
        } else {
            utilisateur = connecte;
        }

        Materiel materiel = materielRepository.findById(dto.materielId())
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel introuvable"));


        // Vérification de la disponibilité du matériel sur la période demandée
        boolean disponible = materielRepository
                .retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                        dto.dateDebut(),
                        dto.dateFin(),
                        materiel.getModele().getCategorieModele().getId()
                )
                .stream()
                .anyMatch(m -> m.getId().equals(dto.materielId()));

        if (!disponible) {
            throw new ConflitReservationException("Matériel indisponible sur la période demandée");
        }

        Emprunt emprunt = empruntMapper.toEntity(dto);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setMateriel(materiel);
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_EN_COURS));

        return empruntMapper.toDto(empruntRepository.save(emprunt));
    }

    // =========================================================
    // Modification
    // =========================================================

    /**
     * Met à jour les champs scalaires et les associations d'un emprunt existant.
     *
     * <p>La mise à jour du statut passe par {@link #modifierStatutEmprunt} ou
     * les endpoints dédiés (prolongation, retour anticipé).
     *
     * @param id  identifiant de l'emprunt à modifier
     * @param dto les nouvelles données
     * @throws RessourceIntrouvableException si l'emprunt, le matériel ou l'utilisateur est introuvable
     */
    @Transactional
    @Override
    public void modifierEmprunt(Integer id, EmpruntRequestDto dto) {

        empruntValidator.validerCreationOuModification(dto);

        Emprunt emprunt = getEmpruntOuException(id);

        Materiel materiel = materielRepository.findById(dto.materielId())
                .orElseThrow(() -> new RessourceIntrouvableException("Matériel introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(dto.utilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Utilisateur introuvable"));

        // Le mapper met à jour les champs scalaires ; le service met à jour les associations
        empruntMapper.update(dto, emprunt);
        emprunt.setMateriel(materiel);
        emprunt.setUtilisateur(utilisateur);

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // Changement de statut (admin)
    // =========================================================

    /**
     * Modifie le statut d'un emprunt vers un statut de décision simple
     * (accepté, refusé, prolongation refusée, retour anticipé refusé).
     *
     * <p>Les transitions complexes (prolongation, retour anticipé)
     * passent par leurs méthodes dédiées.
     *
     * @param empruntId identifiant de l'emprunt
     * @param dto       le nouveau code statut
     * @throws MetierException si le code est inconnu ou non autorisé ici
     */
    @Transactional
    @Override
    public void modifierStatutEmprunt(int empruntId, EmpruntStatutRequestDto dto) {

        Emprunt emprunt = getEmpruntOuException(empruntId);

        CodeStatutEmprunt code;
        try {
            code = CodeStatutEmprunt.valueOf(dto.code());

        } catch (IllegalArgumentException _) {
            throw new RessourceIntrouvableException("Code statut inconnu : " + dto.code());
        }

        // Seuls ces codes sont autorisés via cet endpoint générique
        switch (code) {
            case DEMANDE_EN_COURS, ACCEPTE, REFUSE,
                 PROLONGATION_REFUSE, DEMANDE_RETOUR_ANTICIPE_REFUSE -> {
                // Statuts autorisés : aucune action supplémentaire nécessaire
            }
            default -> throw new MetierException(
                    "Utilisez les endpoints dédiés pour la transition de statut : " + code
            );
        }

        emprunt.setStatutEmprunt(getStatutParCode(code));
        empruntRepository.save(emprunt);
    }

    // =========================================================
    // Prolongation
    // =========================================================

    /**
     * Enregistre une demande de prolongation sur un emprunt accepté.
     *
     * @param id  identifiant de l'emprunt
     * @param dto la date de prolongation souhaitée
     * @throws MetierException si la date est absente ou invalide
     */
    @Transactional
    @Override
    public void demanderProlongation(int id, DemandeProlongationRequestDto dto) {

        Emprunt emprunt = getEmpruntOuException(id);

        empruntValidator.validerProlongation(emprunt, dto.dateProlongation());

        emprunt.setDateProlongation(dto.dateProlongation());
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_PROLONGATION));

        empruntRepository.save(emprunt);
    }

    /**
     * Accepte une demande de prolongation : la date de fin devient la date de prolongation.
     *
     * @param id identifiant de l'emprunt
     * @throws MetierException si aucune prolongation n'est en attente
     */
    @Transactional
    @Override
    public void accepterProlongation(int id) {

        Emprunt emprunt = getEmpruntOuException(id);

        if (emprunt.getDateProlongation() == null) {
            throw new MetierException("Aucune prolongation en attente sur cet emprunt");
        }

        // La date de fin est repoussée à la date de prolongation acceptée
        emprunt.setDateFin(emprunt.getDateProlongation());
        emprunt.setDateProlongation(null);
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.PROLONGATION_ACCEPTE));

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // Retour anticipé
    // =========================================================

    /**
     * Enregistre une demande de retour anticipé sur un emprunt en cours.
     *
     * @param id  identifiant de l'emprunt
     * @param dto la date de retour anticipé souhaitée
     * @throws MetierException si la date est absente ou invalide
     */
    @Transactional
    @Override
    public void demanderRetourAnticipe(int id, DemandeRetourAnticipeRequestDto dto) {

        Emprunt emprunt = getEmpruntOuException(id);

        empruntValidator.validerRetourAnticipe(emprunt, dto.dateRetourAnticipe());

        emprunt.setDateRetourAnticipe(dto.dateRetourAnticipe());
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE));

        empruntRepository.save(emprunt);
    }

    /**
     * Accepte un retour anticipé : la date de fin est avancée à la date de retour anticipé.
     *
     * @param id identifiant de l'emprunt
     * @throws MetierException si aucun retour anticipé n'est en attente
     */
    @Transactional
    @Override
    public void accepterRetourAnticipe(int id) {

        Emprunt emprunt = getEmpruntOuException(id);

        if (emprunt.getDateRetourAnticipe() == null) {
            throw new MetierException("Aucun retour anticipé en attente sur cet emprunt");
        }

        // La date de fin est avancée à la date de retour anticipé acceptée
        emprunt.setDateFin(emprunt.getDateRetourAnticipe());
        emprunt.setDateRetourAnticipe(null);
        emprunt.setStatutEmprunt(getStatutParCode(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE_ACCEPTE));

        empruntRepository.save(emprunt);
    }

    // =========================================================
    // Suppression
    // =========================================================

    @Override
    public void supprimerEmprunt(Integer id) {
        if (!empruntRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Emprunt introuvable : " + id);
        }
        empruntRepository.deleteById(id);
    }
}
