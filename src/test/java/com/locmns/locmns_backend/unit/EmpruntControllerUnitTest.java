package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.controller.EmpruntController;
import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mock.MockEmpruntRepository;
import com.locmns.locmns_backend.mock.MockMaterielRepository;
import com.locmns.locmns_backend.mock.MockUtilisateurRepository;
import com.locmns.locmns_backend.repository.StatutEmpruntRepository;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.services.IMaterielService;
import com.locmns.locmns_backend.services.MaterielService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmpruntControllerUnitTest {

    private EmpruntController empruntController;
    private MockEmpruntRepository mockEmpruntDao;

    StatutEmpruntRepository mockStatutEmpruntRepository = Mockito.mock(StatutEmpruntRepository.class);
    IMaterielService mockIMaterielService = Mockito.mock(MaterielService.class);


    @BeforeEach
    public void init() {
        mockEmpruntDao = new MockEmpruntRepository();
        MockMaterielRepository mockMaterielDao = new MockMaterielRepository();
        MockUtilisateurRepository mockUtilisateurDao = new MockUtilisateurRepository();

        StatutEmpruntRepository mockStatutEmpruntRepository = Mockito.mock(StatutEmpruntRepository.class);
        MaterielService mockMaterielService = Mockito.mock(MaterielService.class);

        EmpruntService empruntService = new EmpruntService(
                mockEmpruntDao,
                mockMaterielDao,
                mockUtilisateurDao,
                mockStatutEmpruntRepository,
                mockMaterielService
        );

        empruntController = new EmpruntController(empruntService);
    }

    // ── obtenirEmpruntParId ───────────────────────────────

    @Test
    public void obtenirEmpruntParIdExistant_devrait_Retourner200() {
        ResponseEntity<EmpruntResponseDto> reponse = empruntController.obtenirEmpruntParId(1);
        assertEquals(HttpStatus.OK, reponse.getStatusCode());
    }

    @Test
    public void obtenirEmpruntParIdInexistant_devrait_Retourner404() {
        ResponseEntity<EmpruntResponseDto> reponse = empruntController.obtenirEmpruntParId(99);
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode());
    }

    // ── supprimerEmprunt ──────────────────────────────────

    @Test
    public void supprimerEmpruntExistant_devrait_Retourner204() {
        ResponseEntity<Void> reponse = empruntController.supprimerEmprunt(1);
        assertEquals(HttpStatus.NO_CONTENT, reponse.getStatusCode());
    }

    @Test
    public void supprimerEmpruntInexistant_devrait_lancerException() {
        assertThrows(RessourceIntrouvableException.class, () -> {
            empruntController.supprimerEmprunt(99);
        });
    }

    // ── modifierEmprunt ───────────────────────────────────

    @Test
    public void modifierEmpruntExistant_devrait_Retourner204() {
        EmpruntRequestDto empruntRequestDto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );
        ResponseEntity<Void> reponse = empruntController.modifierEmprunt(1, empruntRequestDto);
        assertEquals(HttpStatus.NO_CONTENT, reponse.getStatusCode());
    }

    @Test
    public void modifierEmpruntInexistant_devrait_lancerException() {
        EmpruntRequestDto empruntRequestDto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );

        assertThrows(RessourceIntrouvableException.class, () -> {
            empruntController.modifierEmprunt(99, empruntRequestDto);
        });
    }
}