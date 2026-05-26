package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.controller.EmpruntController;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mock.MockEmpruntDao;
import com.locmns.locmns_backend.mock.MockMaterielDao;
import com.locmns.locmns_backend.mock.MockUtilisateurDao;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.services.MaterielService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmpruntControllerUnitTest {

    private EmpruntController empruntController;
    private MockEmpruntDao mockEmpruntDao;


    @BeforeEach
    public void init() {
        mockEmpruntDao = new MockEmpruntDao();
        MockMaterielDao mockMaterielDao = new MockMaterielDao();
        MockUtilisateurDao mockUtilisateurDao = new MockUtilisateurDao();

        EmpruntService empruntService = new EmpruntService(
                mockEmpruntDao,
                mockMaterielDao,
                mockUtilisateurDao,
                null
        );

        MaterielService materielService = new MaterielService(
                mockMaterielDao,
                null,
                null,
                null,
                mockEmpruntDao
        );

        empruntController = new EmpruntController(empruntService, materielService);
    }

    // ── obtenirEmpruntParId ───────────────────────────────

    @Test
    public void obtenirEmpruntParIdExistant_devrait_Retourner200() {
        ResponseEntity<Emprunt> reponse = empruntController.obtenirEmpruntParId(1);
        assertEquals(HttpStatus.OK, reponse.getStatusCode());
    }

    @Test
    public void obtenirEmpruntParIdInexistant_devrait_Retourner404() {
        ResponseEntity<Emprunt> reponse = empruntController.obtenirEmpruntParId(99);
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
        EmpruntDto empruntDto = new EmpruntDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );
        ResponseEntity<Void> reponse = empruntController.modifierEmprunt(1, empruntDto);
        assertEquals(HttpStatus.NO_CONTENT, reponse.getStatusCode());
    }

    @Test
    public void modifierEmpruntInexistant_devrait_lancerException() {
        EmpruntDto empruntDto = new EmpruntDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );

        assertThrows(RessourceIntrouvableException.class, () -> {
            empruntController.modifierEmprunt(99, empruntDto);
        });
    }
}