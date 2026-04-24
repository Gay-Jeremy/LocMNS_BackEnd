package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.controller.EmpruntController;
import com.locmns.locmns_backend.mock.MockEmpruntDao;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.services.EmpruntService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmpruntControllerUnitTest {

    private EmpruntController empruntController;
    private MockEmpruntDao mockEmpruntDao;

    @BeforeEach
    public void init() {
        mockEmpruntDao = new MockEmpruntDao();
        EmpruntService empruntService = new EmpruntService(mockEmpruntDao, null, null, null);
        empruntController = new EmpruntController(mockEmpruntDao, null, null, null, empruntService);
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
    public void supprimerEmpruntInexistant_devrait_Retourner404() {
        ResponseEntity<Void> reponse = empruntController.supprimerEmprunt(99);
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode());
    }

    // ── modifierEmprunt ───────────────────────────────────

    @Test
    public void modifierEmpruntExistant_devrait_Retourner204() {
        Emprunt emprunt = new Emprunt();
        ResponseEntity<Void> reponse = empruntController.modifierEmprunt(1, emprunt);
        assertEquals(HttpStatus.NO_CONTENT, reponse.getStatusCode());
    }

    @Test
    public void modifierEmpruntInexistant_devrait_Retourner404() {
        Emprunt emprunt = new Emprunt();
        ResponseEntity<Void> reponse = empruntController.modifierEmprunt(99, emprunt);
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode());
    }
}