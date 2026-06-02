package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.repository.EmpruntRepository;
import com.locmns.locmns_backend.repository.MaterielRepository;
import com.locmns.locmns_backend.repository.StatutEmpruntRepository;
import com.locmns.locmns_backend.repository.UtilisateurRepository;
import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.services.IEmpruntService;
import com.locmns.locmns_backend.services.MaterielService;
import com.locmns.locmns_backend.validator.emprunt.EmpruntValidator;
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
    private EmpruntRepository empruntRepository;
    private MaterielRepository materielRepository;
    private UtilisateurRepository utilisateurRepository;
    private StatutEmpruntRepository statutEmpruntRepository;
    private MaterielService materielService;

    private IEmpruntService IEmpruntService;

    // Données réutilisées dans plusieurs tests
    private Materiel materielDisponible;
    private Utilisateur utilisateur;
    private StatutEmprunt statutEmprunt;
    private EmpruntRequestDto dto;

    @BeforeEach
    public void init() {
        // Mockito crée de faux DAOs — pas de base de données
        empruntRepository = Mockito.mock(EmpruntRepository.class);
        materielRepository = Mockito.mock(MaterielRepository.class);
        utilisateurRepository = Mockito.mock(UtilisateurRepository.class);
        statutEmpruntRepository = Mockito.mock(StatutEmpruntRepository.class);
        materielService = Mockito.mock(MaterielService.class);

        EmpruntValidator empruntValidator = new EmpruntValidator();

        IEmpruntService = new EmpruntService(
                empruntRepository,
                materielRepository,
                utilisateurRepository,
                statutEmpruntRepository,
                materielService,
                empruntValidator
        );


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

        dto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );

        Emprunt empruntSauvegarde = new Emprunt();
        empruntSauvegarde.setId(1);
        empruntSauvegarde.setDateDebut(dto.dateDebut());
        empruntSauvegarde.setDateFin(dto.dateFin());
        empruntSauvegarde.setMateriel(materielDisponible);
        empruntSauvegarde.setUtilisateur(utilisateur);
        empruntSauvegarde.setStatutEmprunt(statutEmprunt);

        when(empruntRepository.save(any())).thenReturn(empruntSauvegarde);

    }

    // ── CAS NOMINAL ──────────────────────────────────────

    @Test
    public void creerEmpruntAvecDonneesValides_devrait_Reussir() {

        when(materielRepository.findById(1)).thenReturn(Optional.of(materielDisponible));
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                any(), any(), eq(1)
        )).thenReturn(List.of(materielDisponible));
        when(statutEmpruntRepository.findByCode("DEMANDE_EN_COURS")).thenReturn(Optional.of(statutEmprunt));

        // Act
        assertDoesNotThrow(() -> IEmpruntService.creerEmprunt(dto));

        // Assert — save() a bien été appelé une fois
        verify(empruntRepository, times(1)).save(any(Emprunt.class));
    }

    // ── VALIDATION DES DATES ─────────────────────────────

    @Test
    public void creerEmpruntAvecDateFinAvantDateDebut_devrait_LeverMetierException() {
        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now().plusDays(7), // dateDebut après dateFin
                LocalDate.now(),
                1,
                1,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> IEmpruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("La date de fin doit être après la date de début", exception.getMessage());
    }

    @Test
    public void creerEmpruntAvecDateDebutDansLePasse_devrait_LeverMetierException() {

        when(materielRepository.findById(1))
                .thenReturn(Optional.of(materielDisponible));

        when(utilisateurRepository.findById(1))
                .thenReturn(Optional.of(utilisateur));

        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> IEmpruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals(
                "La date de début ne peut pas être dans le passé",
                exception.getMessage()
        );
    }

    @Test
    public void creerEmpruntAvecDateFinDansLePasse_devrait_LeverMetierException() {
        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1), // aussi dans le passé
                1,
                1,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> IEmpruntService.creerEmprunt(dtoInvalide)
        );

        // dateDebut dans le passé est vérifiée en premier
        assertEquals("La date de début ne peut pas être dans le passé", exception.getMessage());
    }

    // ── EXISTENCE DES ENTITÉS ────────────────────────────

    @Test
    public void creerEmpruntAvecMaterielInexistant_devrait_LeverMetierException() {
        // Le mock retourne vide — matériel introuvable
        when(materielRepository.findById(99)).thenReturn(Optional.empty());

        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                99, // id inexistant
                1,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> IEmpruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("Matériel introuvable", exception.getMessage());
    }

    @Test
    public void creerEmpruntAvecUtilisateurInexistant_devrait_LeverMetierException() {
        when(materielRepository.findById(1)).thenReturn(Optional.of(materielDisponible));
        when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());

        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                99, // id inexistant
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> IEmpruntService.creerEmprunt(dtoInvalide)
        );

        assertEquals("Utilisateur introuvable", exception.getMessage());
    }

    // ── DISPONIBILITÉ ────────────────────────────────────

    @Test
    public void creerEmpruntAvecMaterielIndisponible_devrait_LeverMetierException() {
        when(materielRepository.findById(1)).thenReturn(Optional.of(materielDisponible));
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        // Liste vide — matériel non disponible sur la période
        when(materielRepository.retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                any(), any(), eq(1)
        )).thenReturn(List.of());

        MetierException exception = assertThrows(
                MetierException.class,
                () -> IEmpruntService.creerEmprunt(dto)
        );

        assertEquals("Matériel indisponible", exception.getMessage());
    }
}
