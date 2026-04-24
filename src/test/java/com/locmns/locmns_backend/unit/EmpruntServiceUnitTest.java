package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.dao.EmpruntDao;
import com.locmns.locmns_backend.dao.MaterielDao;
import com.locmns.locmns_backend.dao.StatutEmpruntDao;
import com.locmns.locmns_backend.dao.UtilisateurDao;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.services.EmpruntService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmpruntServiceUnitTest {

    // Les mocks — faux DAOs générés par Mockito
    private EmpruntDao empruntDao;
    private MaterielDao materielDao;
    private UtilisateurDao utilisateurDao;
    private StatutEmpruntDao statutEmpruntDao;

    private EmpruntService empruntService;

    // Données réutilisées dans plusieurs tests
    private Materiel materielDisponible;
    private Utilisateur utilisateur;
    private StatutEmprunt statutEmprunt;
    private EmpruntDto dto;

    @BeforeEach
    public void init() {
        // Mockito crée de faux DAOs — pas de base de données
        empruntDao      = Mockito.mock(EmpruntDao.class);
        materielDao     = Mockito.mock(MaterielDao.class);
        utilisateurDao  = Mockito.mock(UtilisateurDao.class);
        statutEmpruntDao = Mockito.mock(StatutEmpruntDao.class);

        empruntService = new EmpruntService(empruntDao, materielDao, utilisateurDao, statutEmpruntDao);

        // Données communes à tous les tests
        CategorieModele categorieModele = new CategorieModele();
        categorieModele.setId(1);

        Modele modele = new Modele();
        modele.setCategorieModele(categorieModele);

        materielDisponible = new Materiel();
        materielDisponible.setId(1);
        materielDisponible.setModele(modele);

        utilisateur = new Utilisateur();
        utilisateur.setId(1);

        statutEmprunt = new StatutEmprunt();
        statutEmprunt.setId(3);

        dto = new EmpruntDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1
        );
    }

    // ── CAS NOMINAL ──────────────────────────────────────

    @Test
    public void creerEmpruntAvecDonneesValides_devrait_Reussir() {

        // Arrange — on dit aux mocks quoi retourner
        when(materielDao.findById(1)).thenReturn(Optional.of(materielDisponible));
        when(utilisateurDao.findById(1)).thenReturn(Optional.of(utilisateur));
        when(materielDao.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                any(), any(), eq(1)
        )).thenReturn(List.of(materielDisponible));
        when(statutEmpruntDao.findById(3)).thenReturn(Optional.of(statutEmprunt));

        // Act
        assertDoesNotThrow(() -> empruntService.creerEmprunt(dto));

        // Assert — save() a bien été appelé une fois
        verify(empruntDao, times(1)).save(any(Emprunt.class));
    }

    // ── VALIDATION DES DATES ─────────────────────────────

    @Test
    public void creerEmpruntAvecDateFinAvantDateDebut_devrait_LeverMetierException() {
        EmpruntDto dtoInvalide = new EmpruntDto(
                LocalDate.now().plusDays(7), // dateDebut après dateFin
                LocalDate.now(),
                1,
                1
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("La date de fin doit être après la date de début", exception.getMessage());
    }

    @Test
    public void creerEmpruntAvecDateDebutDansLePasse_devrait_LeverMetierException() {
        EmpruntDto dtoInvalide = new EmpruntDto(
                LocalDate.now().minusDays(1), // hier
                LocalDate.now().plusDays(7),
                1,
                1
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("La date de début ne peut pas être dans le passé", exception.getMessage());
    }

    @Test
    public void creerEmpruntAvecDateFinDansLePasse_devrait_LeverMetierException() {
        EmpruntDto dtoInvalide = new EmpruntDto(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1), // aussi dans le passé
                1,
                1
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide)
        );

        // dateDebut dans le passé est vérifiée en premier
        assertEquals("La date de début ne peut pas être dans le passé", exception.getMessage());
    }

    // ── EXISTENCE DES ENTITÉS ────────────────────────────

    @Test
    public void creerEmpruntAvecMaterielInexistant_devrait_LeverMetierException() {
        // Le mock retourne vide — matériel introuvable
        when(materielDao.findById(99)).thenReturn(Optional.empty());

        EmpruntDto dtoInvalide = new EmpruntDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                99, // id inexistant
                1
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("Matériel introuvable", exception.getMessage());
    }

    @Test
    public void creerEmpruntAvecUtilisateurInexistant_devrait_LeverMetierException() {
        when(materielDao.findById(1)).thenReturn(Optional.of(materielDisponible));
        when(utilisateurDao.findById(99)).thenReturn(Optional.empty());

        EmpruntDto dtoInvalide = new EmpruntDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                99 // id inexistant
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("Utilisateur introuvable", exception.getMessage());
    }

    // ── DISPONIBILITÉ ────────────────────────────────────

    @Test
    public void creerEmpruntAvecMaterielIndisponible_devrait_LeverMetierException() {
        when(materielDao.findById(1)).thenReturn(Optional.of(materielDisponible));
        when(utilisateurDao.findById(1)).thenReturn(Optional.of(utilisateur));
        // Liste vide — matériel non disponible sur la période
        when(materielDao.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                any(), any(), eq(1)
        )).thenReturn(List.of());

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dto)
        );

        assertEquals("Ce matériel n'est pas disponible sur cette période", exception.getMessage());
    }
}
