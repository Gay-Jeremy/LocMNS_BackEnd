package com.locmns.locmns_backend.unit;

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
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.validator.emprunt.EmpruntValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    @Mock private EmpruntRepository empruntRepository;
    @Mock private MaterielRepository materielRepository;
    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private StatutEmpruntRepository statutEmpruntRepository;
    @Mock private EmpruntValidator empruntValidator;
    @Mock private EmpruntMapper empruntMapper;

    @InjectMocks
    private EmpruntService empruntService;

    // ── helpers ──────────────────────────────────────────────

    private Utilisateur adminUser() {
        CategorieUtilisateur cat = new CategorieUtilisateur();
        cat.setCode(Role.ADMIN.name());
        Utilisateur u = new Utilisateur();
        u.setEmail("admin@test.com");
        u.setCategorieUtilisateur(cat);
        return u;
    }

    private Utilisateur regularUser() {
        CategorieUtilisateur cat = new CategorieUtilisateur();
        cat.setCode(Role.USER.name());
        Utilisateur u = new Utilisateur();
        u.setEmail("user@test.com");
        u.setCategorieUtilisateur(cat);
        return u;
    }

    private Utilisateur utilisateurAvecId(int id) {
        Utilisateur u = regularUser();
        u.setId(id);
        return u;
    }

    private StatutEmprunt statut(CodeStatutEmprunt code) {
        StatutEmprunt s = new StatutEmprunt();
        s.setCode(code.name());
        return s;
    }

    private Emprunt empruntAvecStatut(CodeStatutEmprunt code) {
        Emprunt e = new Emprunt();
        e.setStatutEmprunt(statut(code));
        e.setDateDebut(LocalDate.now().plusDays(1));
        e.setDateFin(LocalDate.now().plusDays(10));
        return e;
    }

    // =========================================================
    // Lecture
    // =========================================================

    @Nested
    @DisplayName("obtenirEmpruntsParId")
    class ObtenirParId {

        @Test
        @DisplayName("Retourne le DTO si l'emprunt existe")
        void trouvé() {
            Emprunt emprunt = new Emprunt();
            EmpruntResponseDto dto = mock(EmpruntResponseDto.class);
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            when(empruntMapper.toDto(emprunt)).thenReturn(dto);

            Optional<EmpruntResponseDto> result = empruntService.obtenirEmpruntsParId(1, adminUser());

            assertThat(result).contains(dto);
        }

        @Test
        @DisplayName("Utilisateur : l'emprunt d'un autre utilisateur est invisible")
        void empruntDUnAutreUtilisateur() {
            Emprunt emprunt = new Emprunt();
            emprunt.setUtilisateur(utilisateurAvecId(2));
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));

            assertThat(empruntService.obtenirEmpruntsParId(1, utilisateurAvecId(3))).isEmpty();
            verify(empruntMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Utilisateur : voit son propre emprunt")
        void sonPropreEmprunt() {
            Emprunt emprunt = new Emprunt();
            emprunt.setUtilisateur(utilisateurAvecId(3));
            EmpruntResponseDto dto = mock(EmpruntResponseDto.class);
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            when(empruntMapper.toDto(emprunt)).thenReturn(dto);

            assertThat(empruntService.obtenirEmpruntsParId(1, utilisateurAvecId(3))).contains(dto);
        }

        @Test
        @DisplayName("Retourne Optional.empty() si l'emprunt est absent")
        void absent() {
            when(empruntRepository.findById(99)).thenReturn(Optional.empty());

            assertThat(empruntService.obtenirEmpruntsParId(99, adminUser())).isEmpty();
        }
    }

    @Nested
    @DisplayName("obtenirTousLesEmprunts")
    class ObtenirTous {

        @Test
        @DisplayName("Admin : retourne la liste complète triée par dateCreation")
        void retourneListe() {
            List<Emprunt> emprunts = List.of(new Emprunt());
            List<EmpruntResponseDto> dtos = List.of(mock(EmpruntResponseDto.class));
            when(empruntRepository.findAll(any(Sort.class))).thenReturn(emprunts);
            when(empruntMapper.toDtoList(emprunts)).thenReturn(dtos);

            assertThat(empruntService.obtenirTousLesEmprunts(adminUser())).isEqualTo(dtos);
        }

        @Test
        @DisplayName("Utilisateur : retourne uniquement ses propres emprunts")
        void utilisateurVoitSesEmprunts() {
            Utilisateur user = regularUser();
            List<Emprunt> emprunts = List.of(new Emprunt());
            when(empruntRepository.retourneListeEmpruntParEmail(user.getEmail())).thenReturn(emprunts);
            when(empruntMapper.toDtoList(emprunts)).thenReturn(List.of());

            empruntService.obtenirTousLesEmprunts(user);

            verify(empruntRepository).retourneListeEmpruntParEmail(user.getEmail());
            verify(empruntRepository, never()).findAll(any(Sort.class));
        }
    }

    @Nested
    @DisplayName("obtenirEmpruntSelonRole")
    class ObtenirSelonRole {

        @Test
        @DisplayName("Admin : appelle retourneListeEmpruntActif")
        void admin() {
            Utilisateur admin = adminUser();
            List<Emprunt> emprunts = List.of(new Emprunt());
            when(empruntRepository.retourneListeEmpruntActif(anyList())).thenReturn(emprunts);
            when(empruntMapper.toDtoList(emprunts)).thenReturn(List.of());

            empruntService.obtenirEmpruntSelonRole(admin);

            verify(empruntRepository).retourneListeEmpruntActif(anyList());
            verify(empruntRepository, never()).retourneListeEmpruntActifParEmail(anyList(), anyString());
        }

        @Test
        @DisplayName("User : appelle retourneListeEmpruntActifParEmail")
        void user() {
            Utilisateur user = regularUser();
            List<Emprunt> emprunts = List.of();
            when(empruntRepository.retourneListeEmpruntActifParEmail(anyList(), eq(user.getEmail())))
                    .thenReturn(emprunts);
            when(empruntMapper.toDtoList(emprunts)).thenReturn(List.of());

            empruntService.obtenirEmpruntSelonRole(user);

            verify(empruntRepository).retourneListeEmpruntActifParEmail(anyList(), eq(user.getEmail()));
            verify(empruntRepository, never()).retourneListeEmpruntActif(anyList());
        }
    }

    // =========================================================
    // Création
    // =========================================================

    @Nested
    @DisplayName("creerEmprunt")
    class CreerEmprunt {

        private EmpruntRequestDto dto;
        private Materiel materiel;
        private StatutEmprunt statutDemandeEnCours;

        @BeforeEach
        void setUp() {
            dto = mock(EmpruntRequestDto.class);
            lenient().when(dto.materielId()).thenReturn(1);
            lenient().when(dto.dateDebut()).thenReturn(LocalDate.now().plusDays(1));
            lenient().when(dto.dateFin()).thenReturn(LocalDate.now().plusDays(5));

            CategorieModele categorieModele = new CategorieModele();
            categorieModele.setId(10);
            Modele modele = new Modele();
            modele.setCategorieModele(categorieModele);
            materiel = new Materiel();
            materiel.setId(1);
            materiel.setModele(modele);

            statutDemandeEnCours = statut(CodeStatutEmprunt.DEMANDE_EN_COURS);
        }

        @Test
        @DisplayName("Admin sans utilisateurId → MetierException")
        void adminSansUtilisateurId() {
            Utilisateur admin = adminUser();
            when(dto.utilisateurId()).thenReturn(null);

            assertThatThrownBy(() -> empruntService.creerEmprunt(dto, admin))
                    .isInstanceOf(MetierException.class);
        }

        @Test
        @DisplayName("Admin avec utilisateur introuvable → RessourceIntrouvableException")
        void adminUtilisateurIntrouvable() {
            Utilisateur admin = adminUser();
            when(dto.utilisateurId()).thenReturn(99);
            when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> empruntService.creerEmprunt(dto, admin))
                    .isInstanceOf(RessourceIntrouvableException.class);
        }

        @Test
        @DisplayName("Matériel introuvable → RessourceIntrouvableException")
        void materielIntrouvable() {
            Utilisateur user = regularUser();
            when(materielRepository.findById(1)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> empruntService.creerEmprunt(dto, user))
                    .isInstanceOf(RessourceIntrouvableException.class);
        }

        @Test
        @DisplayName("Matériel indisponible → ConflitReservationException")
        void materielIndisponible() {
            Utilisateur user = regularUser();
            when(materielRepository.findById(1)).thenReturn(Optional.of(materiel));
            when(materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                    any(), any(), eq(10))).thenReturn(List.of()); // liste vide = indisponible

            assertThatThrownBy(() -> empruntService.creerEmprunt(dto, user))
                    .isInstanceOf(ConflitReservationException.class);
        }

        @Test
        @DisplayName("Création réussie pour un utilisateur normal")
        void creationReussie() {
            Utilisateur user = regularUser();
            Emprunt emprunt = new Emprunt();
            EmpruntResponseDto responseDto = mock(EmpruntResponseDto.class);

            when(materielRepository.findById(1)).thenReturn(Optional.of(materiel));

            Materiel materielDisponible = new Materiel();
            materielDisponible.setId(1);
            when(materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                    any(), any(), eq(10))).thenReturn(List.of(materielDisponible));

            when(empruntMapper.toEntity(dto)).thenReturn(emprunt);
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_EN_COURS.name()))
                    .thenReturn(Optional.of(statutDemandeEnCours));
            when(empruntRepository.save(emprunt)).thenReturn(emprunt);
            when(empruntMapper.toDto(emprunt)).thenReturn(responseDto);

            EmpruntResponseDto result = empruntService.creerEmprunt(dto, user);

            assertThat(result).isEqualTo(responseDto);
            assertThat(emprunt.getUtilisateur()).isEqualTo(user);
            assertThat(emprunt.getMateriel()).isEqualTo(materiel);
            assertThat(emprunt.getStatutEmprunt()).isEqualTo(statutDemandeEnCours);
        }

        @Test
        @DisplayName("Admin crée un emprunt pour un autre utilisateur")
        void adminCreePourAutreUtilisateur() {
            Utilisateur admin = adminUser();
            Utilisateur cible = regularUser();
            Emprunt emprunt = new Emprunt();

            when(dto.utilisateurId()).thenReturn(2);
            when(utilisateurRepository.findById(2)).thenReturn(Optional.of(cible));
            when(materielRepository.findById(1)).thenReturn(Optional.of(materiel));

            Materiel disponible = new Materiel();
            disponible.setId(1);
            when(materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                    any(), any(), eq(10))).thenReturn(List.of(disponible));

            when(empruntMapper.toEntity(dto)).thenReturn(emprunt);
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_EN_COURS.name()))
                    .thenReturn(Optional.of(statutDemandeEnCours));
            when(empruntRepository.save(emprunt)).thenReturn(emprunt);
            when(empruntMapper.toDto(emprunt)).thenReturn(mock(EmpruntResponseDto.class));

            empruntService.creerEmprunt(dto, admin);

            assertThat(emprunt.getUtilisateur()).isEqualTo(cible);
        }
    }

    // =========================================================
    // Modification du statut
    // =========================================================

    @Nested
    @DisplayName("modifierStatutEmprunt")
    class ModifierStatut {

        @Test
        @DisplayName("Code inconnu → RessourceIntrouvableException")
        void codeInconnu() {
            Emprunt emprunt = new Emprunt();
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            EmpruntStatutRequestDto dto = mock(EmpruntStatutRequestDto.class);
            when(dto.code()).thenReturn("CODE_INEXISTANT");

            assertThatThrownBy(() -> empruntService.modifierStatutEmprunt(1, dto))
                    .isInstanceOf(RessourceIntrouvableException.class);
        }

        @Test
        @DisplayName("Code non autorisé via cet endpoint → MetierException")
        void codeNonAutorisé() {
            Emprunt emprunt = new Emprunt();
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            EmpruntStatutRequestDto dto = mock(EmpruntStatutRequestDto.class);
            when(dto.code()).thenReturn(CodeStatutEmprunt.PROLONGATION_ACCEPTE.name());

            assertThatThrownBy(() -> empruntService.modifierStatutEmprunt(1, dto))
                    .isInstanceOf(MetierException.class)
                    .hasMessageContaining("endpoints dédiés");
        }

        @Test
        @DisplayName("Code ACCEPTE autorisé → statut mis à jour")
        void codeAutorisé() {
            Emprunt emprunt = new Emprunt();
            StatutEmprunt statutAccepte = statut(CodeStatutEmprunt.ACCEPTE);
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            EmpruntStatutRequestDto dto = mock(EmpruntStatutRequestDto.class);
            when(dto.code()).thenReturn(CodeStatutEmprunt.ACCEPTE.name());
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.ACCEPTE.name()))
                    .thenReturn(Optional.of(statutAccepte));

            empruntService.modifierStatutEmprunt(1, dto);

            assertThat(emprunt.getStatutEmprunt()).isEqualTo(statutAccepte);
            verify(empruntRepository).save(emprunt);
        }
    }

    // =========================================================
    // Prolongation
    // =========================================================

    @Nested
    @DisplayName("demanderProlongation")
    class DemanderProlongation {

        @Test
        @DisplayName("Emprunt introuvable → RessourceIntrouvableException")
        void empruntIntrouvable() {
            when(empruntRepository.findById(99)).thenReturn(Optional.empty());
            DemandeProlongationRequestDto dto = mock(DemandeProlongationRequestDto.class);

            assertThatThrownBy(() -> empruntService.demanderProlongation(99, dto, adminUser()))
                    .isInstanceOf(RessourceIntrouvableException.class);
        }

        @Test
        @DisplayName("Prolongation valide → statut et date mis à jour")
        void prolongationValide() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.ACCEPTE);
            LocalDate dateProlongation = emprunt.getDateFin().plusDays(5);
            StatutEmprunt statutProlongation = statut(CodeStatutEmprunt.DEMANDE_PROLONGATION);

            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            DemandeProlongationRequestDto dto = mock(DemandeProlongationRequestDto.class);
            when(dto.dateProlongation()).thenReturn(dateProlongation);
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_PROLONGATION.name()))
                    .thenReturn(Optional.of(statutProlongation));

            empruntService.demanderProlongation(1, dto, adminUser());

            assertThat(emprunt.getDateProlongation()).isEqualTo(dateProlongation);
            assertThat(emprunt.getStatutEmprunt()).isEqualTo(statutProlongation);
            verify(empruntRepository).save(emprunt);
        }

        @Test
        @DisplayName("Emprunt d'un autre utilisateur → AccessDeniedException, rien n'est modifié")
        void empruntDUnAutreUtilisateur() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.ACCEPTE);
            emprunt.setUtilisateur(utilisateurAvecId(2));
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            DemandeProlongationRequestDto dto = mock(DemandeProlongationRequestDto.class);

            assertThatThrownBy(() -> empruntService.demanderProlongation(1, dto, utilisateurAvecId(3)))
                    .isInstanceOf(AccessDeniedException.class);
            verify(empruntRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("accepterProlongation")
    class AccepterProlongation {

        @Test
        @DisplayName("Aucune prolongation en attente → MetierException")
        void aucuneProlongation() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.ACCEPTE);
            emprunt.setDateProlongation(null);
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));

            assertThatThrownBy(() -> empruntService.accepterProlongation(1))
                    .isInstanceOf(MetierException.class)
                    .hasMessageContaining("prolongation");
        }

        @Test
        @DisplayName("Prolongation acceptée → dateFin repoussée")
        void prolongationAcceptée() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.DEMANDE_PROLONGATION);
            LocalDate nouvelleFin = emprunt.getDateFin().plusDays(7);
            emprunt.setDateProlongation(nouvelleFin);
            StatutEmprunt statutAccepte = statut(CodeStatutEmprunt.PROLONGATION_ACCEPTE);

            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.PROLONGATION_ACCEPTE.name()))
                    .thenReturn(Optional.of(statutAccepte));

            empruntService.accepterProlongation(1);

            assertThat(emprunt.getDateFin()).isEqualTo(nouvelleFin);
            assertThat(emprunt.getDateProlongation()).isNull();
            assertThat(emprunt.getStatutEmprunt()).isEqualTo(statutAccepte);
        }
    }

    // =========================================================
    // Retour anticipé
    // =========================================================

    @Nested
    @DisplayName("demanderRetourAnticipe")
    class DemanderRetourAnticipe {

        @Test
        @DisplayName("Emprunt introuvable → RessourceIntrouvableException")
        void empruntIntrouvable() {
            when(empruntRepository.findById(99)).thenReturn(Optional.empty());
            DemandeRetourAnticipeRequestDto dto = mock(DemandeRetourAnticipeRequestDto.class);

            assertThatThrownBy(() -> empruntService.demanderRetourAnticipe(99, dto, adminUser()))
                    .isInstanceOf(RessourceIntrouvableException.class);
        }

        @Test
        @DisplayName("Retour anticipé valide → statut et date mis à jour")
        void retourAnticipeValide() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.ACCEPTE);
            LocalDate dateRetour = emprunt.getDateDebut().plusDays(2);
            StatutEmprunt statutRetour = statut(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE);

            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            DemandeRetourAnticipeRequestDto dto = mock(DemandeRetourAnticipeRequestDto.class);
            when(dto.dateRetourAnticipe()).thenReturn(dateRetour);
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE.name()))
                    .thenReturn(Optional.of(statutRetour));

            empruntService.demanderRetourAnticipe(1, dto, adminUser());

            assertThat(emprunt.getDateRetourAnticipe()).isEqualTo(dateRetour);
            assertThat(emprunt.getStatutEmprunt()).isEqualTo(statutRetour);
            verify(empruntRepository).save(emprunt);
        }
    }

    @Nested
    @DisplayName("accepterRetourAnticipe")
    class AccepterRetourAnticipe {

        @Test
        @DisplayName("Aucun retour anticipé en attente → MetierException")
        void aucunRetourAnticipe() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.ACCEPTE);
            emprunt.setDateRetourAnticipe(null);
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));

            assertThatThrownBy(() -> empruntService.accepterRetourAnticipe(1))
                    .isInstanceOf(MetierException.class)
                    .hasMessageContaining("retour anticipé");
        }

        @Test
        @DisplayName("Retour anticipé accepté → dateFin avancée")
        void retourAnticipeAccepté() {
            Emprunt emprunt = empruntAvecStatut(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE);
            LocalDate nouvelleFin = emprunt.getDateDebut().plusDays(3);
            emprunt.setDateRetourAnticipe(nouvelleFin);
            StatutEmprunt statutAccepte = statut(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE_ACCEPTE);

            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt));
            when(statutEmpruntRepository.findByCode(CodeStatutEmprunt.DEMANDE_RETOUR_ANTICIPE_ACCEPTE.name()))
                    .thenReturn(Optional.of(statutAccepte));

            empruntService.accepterRetourAnticipe(1);

            assertThat(emprunt.getDateFin()).isEqualTo(nouvelleFin);
            assertThat(emprunt.getDateRetourAnticipe()).isNull();
            assertThat(emprunt.getStatutEmprunt()).isEqualTo(statutAccepte);
        }
    }

    // =========================================================
    // Suppression
    // =========================================================

    @Nested
    @DisplayName("supprimerEmprunt")
    class SupprimerEmprunt {

        @Test
        @DisplayName("Emprunt introuvable → RessourceIntrouvableException")
        void introuvable() {
            when(empruntRepository.existsById(99)).thenReturn(false);

            assertThatThrownBy(() -> empruntService.supprimerEmprunt(99))
                    .isInstanceOf(RessourceIntrouvableException.class);
        }

        @Test
        @DisplayName("Emprunt existant → deleteById appelé")
        void suppriméAvecSuccès() {
            when(empruntRepository.existsById(1)).thenReturn(true);

            empruntService.supprimerEmprunt(1);

            verify(empruntRepository).deleteById(1);
        }
    }
}