package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.controller.EmpruntController;
import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mock.MockEmpruntRepository;
import com.locmns.locmns_backend.mock.MockMaterielRepository;
import com.locmns.locmns_backend.mock.MockUtilisateurRepository;
import com.locmns.locmns_backend.repository.StatutEmpruntRepository;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.services.MaterielService;
import com.locmns.locmns_backend.validator.emprunt.EmpruntValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EmpruntControllerUnitTest {

    private EmpruntController empruntController;
    private MockEmpruntRepository mockEmpruntDao;

    private final EmpruntValidator mockEmpruntValidator = mock(EmpruntValidator.class);

    @BeforeEach
    public void init() {
        mockEmpruntDao = new MockEmpruntRepository();
        MockMaterielRepository mockMaterielDao = new MockMaterielRepository();
        MockUtilisateurRepository mockUtilisateurDao = new MockUtilisateurRepository();

        StatutEmpruntRepository mockStatutEmpruntRepository = mock(StatutEmpruntRepository.class);
        MaterielService mockMaterielService = mock(MaterielService.class);

        EmpruntService empruntService = new EmpruntService(
                mockEmpruntDao,
                mockMaterielDao,
                mockUtilisateurDao,
                mockStatutEmpruntRepository,
                mockMaterielService,
                mockEmpruntValidator
        );

        empruntController = new EmpruntController(empruntService);
    }

    // ── all ───────────────────────────────────────────────

    @Test
    public void obtenirTousLesEmprunts_devrait_retournerUneListe() {
        List<EmpruntResponseDto> liste = empruntController.all();
        assertNotNull(liste);
    }

    // ── getById ───────────────────────────────────────────

    @Test
    public void obtenirEmpruntParIdExistant_devrait_retournerEmprunt() {
        EmpruntResponseDto reponse = empruntController.getById(1);
        assertNotNull(reponse);
    }

    @Test
    public void obtenirEmpruntParIdInexistant_devrait_lancerException() {
        assertThrows(RuntimeException.class, () -> empruntController.getById(99));
    }

    // ── create ────────────────────────────────────────────

    @Test
    public void creerEmpruntValide_devrait_retournerEmprunt() {
        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                "note test"
        );
        EmpruntResponseDto reponse = empruntController.create(dto);
        assertNotNull(reponse);
    }

    @Test
    public void creerEmpruntAvecDatesInvalides_devrait_lancerException() {
        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.now().plusDays(7),
                LocalDate.now(),           // dateFin avant dateDebut
                1,
                1,
                ""
        );
        assertThrows(Exception.class, () -> empruntController.create(dto));
    }

    // ── update ────────────────────────────────────────────

    @Test
    public void modifierEmpruntExistant_devrait_nesPasLancerException() {
        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );
        assertDoesNotThrow(() -> empruntController.update(1, dto));
    }

    @Test
    public void modifierEmpruntInexistant_devrait_lancerException() {
        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );
        assertThrows(RessourceIntrouvableException.class, () -> empruntController.update(99, dto));
    }

    // ── updateStatut ──────────────────────────────────────

    @Test
    public void modifierStatutAvecCodeValide_devrait_nesPasLancerException() {
        StatutUpdateRequestDto dto = new StatutUpdateRequestDto(1, "ACCEPTE");
        assertDoesNotThrow(() -> empruntController.updateStatut(1, dto));
    }

    @Test
    public void modifierStatutAvecCodeInconnu_devrait_lancerException() {
        StatutUpdateRequestDto dto = new StatutUpdateRequestDto(1, "CODE_INEXISTANT");
        assertThrows(Exception.class, () -> empruntController.updateStatut(1, dto));
    }

    @Test
    public void modifierStatutAvecCodeDedieProlongation_devrait_lancerException() {
        // DEMANDE_PROLONGATION doit passer par l'endpoint dédié, pas /statut
        StatutUpdateRequestDto dto = new StatutUpdateRequestDto(1, "DEMANDE_PROLONGATION");
        assertThrows(Exception.class, () -> empruntController.updateStatut(1, dto));
    }

    // ── demanderProlongation ──────────────────────────────

    @Test
    public void demanderProlongationValide_devrait_nesPasLancerException() {
        DemandeProlongationRequestDto dto = new DemandeProlongationRequestDto(
                LocalDate.now().plusDays(14)
        );
        // Le validateur est mocké donc la validation métier est bypassée
        assertDoesNotThrow(() -> empruntController.demanderProlongation(1, dto));
    }

    @Test
    public void demanderProlongationSansDate_devrait_lancerException() {
        DemandeProlongationRequestDto dto = new DemandeProlongationRequestDto(null);
        assertThrows(Exception.class, () -> empruntController.demanderProlongation(1, dto));
    }

    @Test
    public void demanderProlongationEmpruntInexistant_devrait_lancerException() {
        DemandeProlongationRequestDto dto = new DemandeProlongationRequestDto(
                LocalDate.now().plusDays(14)
        );
        assertThrows(RessourceIntrouvableException.class,
                () -> empruntController.demanderProlongation(99, dto));
    }

    // ── accepterProlongation ──────────────────────────────

    @Test
    public void accepterProlongationSansProlongationEnAttente_devrait_lancerException() {
        // L'emprunt mocké n'a pas de dateProlongation => MetierException attendue
        assertThrows(Exception.class, () -> empruntController.accepterProlongation(1));
    }

    @Test
    public void accepterProlongationEmpruntInexistant_devrait_lancerException() {
        assertThrows(RessourceIntrouvableException.class,
                () -> empruntController.accepterProlongation(99));
    }

    // ── demanderRetourAnticipe ────────────────────────────

    @Test
    public void demanderRetourAnticipeValide_devrait_nesPasLancerException() {
        // La date doit être après dateDebut et avant dateFin de l'emprunt mocké
        DemandeRetourAnticipeRequestDto dto = new DemandeRetourAnticipeRequestDto(
                LocalDate.now().plusDays(3)
        );
        assertDoesNotThrow(() -> empruntController.demanderRetourAnticipe(1, dto));
    }

    @Test
    public void demanderRetourAnticipeSansDate_devrait_lancerException() {
        DemandeRetourAnticipeRequestDto dto = new DemandeRetourAnticipeRequestDto(null);
        assertThrows(Exception.class, () -> empruntController.demanderRetourAnticipe(1, dto));
    }

    @Test
    public void demanderRetourAnticipeEmpruntInexistant_devrait_lancerException() {
        DemandeRetourAnticipeRequestDto dto = new DemandeRetourAnticipeRequestDto(
                LocalDate.now().plusDays(3)
        );
        assertThrows(RessourceIntrouvableException.class,
                () -> empruntController.demanderRetourAnticipe(99, dto));
    }

    // ── accepterRetourAnticipe ────────────────────────────

    @Test
    public void accepterRetourAnticipeSansRetourEnAttente_devrait_lancerException() {
        // L'emprunt mocké n'a pas de dateRetourAnticipe => MetierException attendue
        assertThrows(Exception.class, () -> empruntController.accepterRetourAnticipe(1));
    }

    @Test
    public void accepterRetourAnticipeEmpruntInexistant_devrait_lancerException() {
        assertThrows(RessourceIntrouvableException.class,
                () -> empruntController.accepterRetourAnticipe(99));
    }

    // ── delete ────────────────────────────────────────────

    @Test
    public void supprimerEmpruntExistant_devrait_nesPasLancerException() {
        assertDoesNotThrow(() -> empruntController.delete(1));
    }

    @Test
    public void supprimerEmpruntInexistant_devrait_lancerException() {
        assertThrows(RessourceIntrouvableException.class, () -> empruntController.delete(99));
    }
}