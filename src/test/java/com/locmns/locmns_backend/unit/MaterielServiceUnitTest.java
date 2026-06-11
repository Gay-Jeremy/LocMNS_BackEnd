package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.dto.request.MaterielRequestDto;
import com.locmns.locmns_backend.dto.response.MaterielResponseDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.MaterielMapper;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.*;
import com.locmns.locmns_backend.services.MaterielService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MaterielServiceUnitTest {

    // ── MOCKS ────────────────────────────────────────────────────────────────
    private MaterielRepository      materielRepository;
    private ModeleRepository        modeleRepository;
    private StatutMaterielRepository statutMaterielRepository;
    private SalleRepository         salleRepository;
    private EmpruntRepository       empruntRepository;
    private MaterielMapper          materielMapper;

    // Le vrai service qu'on teste
    private MaterielService materielService;

    // ── DONNÉES COMMUNES ─────────────────────────────────────────────────────
    private Modele          modele;
    private StatutMateriel  statutDisponible;
    private Materiel        materielExistant;
    private Utilisateur     adminUtilisateur;

    @BeforeEach
    public void init() {
        materielRepository      = Mockito.mock(MaterielRepository.class);
        modeleRepository        = Mockito.mock(ModeleRepository.class);
        statutMaterielRepository = Mockito.mock(StatutMaterielRepository.class);
        salleRepository         = Mockito.mock(SalleRepository.class);
        empruntRepository       = Mockito.mock(EmpruntRepository.class);
        materielMapper          = Mockito.mock(MaterielMapper.class);

        materielService = new MaterielService(
                materielRepository,
                modeleRepository,
                statutMaterielRepository,
                salleRepository,
                empruntRepository,
                materielMapper
        );

        // Modèle fictif
        CategorieModele categorieModele = new CategorieModele();
        categorieModele.setId(1);
        categorieModele.setLibelle("Ordinateur portable");

        modele = new Modele();
        modele.setId(1);
        modele.setLibelle("Dell Latitude 5520");
        modele.setCategorieModele(categorieModele);

        // Statut "Disponible"
        statutDisponible = new StatutMateriel();
        statutDisponible.setId(1);
        statutDisponible.setLibelle("Disponible");

        // Matériel fictif déjà "en base"
        materielExistant = new Materiel();
        materielExistant.setId(1);
        materielExistant.setNumSerie("SN-001-TEST");
        materielExistant.setModele(modele);
        materielExistant.setStatutMateriel(statutDisponible);

        // Utilisateur ADMIN fictif
        CategorieUtilisateur categorieAdmin = new CategorieUtilisateur();
        categorieAdmin.setCode("ADMIN");

        adminUtilisateur = new Utilisateur();
        adminUtilisateur.setId(1);
        adminUtilisateur.setEmail("admin@locmns.fr");
        adminUtilisateur.setCategorieUtilisateur(categorieAdmin);

        when(materielMapper.toResponseDto(any(Materiel.class))).thenAnswer(invocation -> {
            Materiel materiel = invocation.getArgument(0);
            return new MaterielResponseDto(materiel.getId(), materiel.getNumSerie(), null, null, null);
        });
        when(materielMapper.toEntity(any(MaterielRequestDto.class))).thenAnswer(invocation -> {
            MaterielRequestDto dto = invocation.getArgument(0);
            Materiel materiel = new Materiel();
            materiel.setNumSerie(dto.numSerie());
            return materiel;
        });
        doAnswer(invocation -> {
            MaterielRequestDto dto = invocation.getArgument(0);
            Materiel materiel = invocation.getArgument(1);
            materiel.setNumSerie(dto.numSerie());
            return null;
        }).when(materielMapper).update(any(MaterielRequestDto.class), any(Materiel.class));
    }

    // ════════════════════════════════════════════════════════════════════════
    // LISTER LES MATÉRIELS
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void obtenirTousLesMateriels_devraitRetournerLaListe() {
        // ARRANGE
        when(materielRepository.findAll()).thenReturn(List.of(materielExistant));

        // ACT
        List<MaterielResponseDto> resultat = materielService.obtenirTousLesMateriels();

        // ASSERT
        assertEquals(1, resultat.size());
        assertEquals("SN-001-TEST", resultat.get(0).numSerie());
    }

    @Test
    public void obtenirMaterielParId_quandExiste_devraitRetournerLeMateriel() {
        // ARRANGE
        when(materielRepository.findById(1)).thenReturn(Optional.of(materielExistant));

        // ACT
        Optional<MaterielResponseDto> resultat = materielService.obtenirMaterielParId(1);

        // ASSERT
        assertTrue(resultat.isPresent());
        assertEquals("SN-001-TEST", resultat.get().numSerie());
    }

    @Test
    public void obtenirMaterielParId_quandInexistant_devraitRetournerVide() {
        // ARRANGE
        when(materielRepository.findById(99)).thenReturn(Optional.empty());

        // ACT
        Optional<MaterielResponseDto> resultat = materielService.obtenirMaterielParId(99);

        // ASSERT
        assertFalse(resultat.isPresent());
    }

    // ════════════════════════════════════════════════════════════════════════
    // CRÉER UN MATÉRIEL
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void creerMateriel_avecNumSerieUnique_devraitReussir() {
        // ARRANGE
        MaterielRequestDto dto = new MaterielRequestDto("SN-002-NOUVEAU", 1, 1, null);

        // Pas de doublon
        when(materielRepository.findByNumSerie("SN-002-NOUVEAU")).thenReturn(Optional.empty());
        when(modeleRepository.findById(1)).thenReturn(Optional.of(modele));
        when(statutMaterielRepository.findById(1)).thenReturn(Optional.of(statutDisponible));
        when(materielRepository.save(any())).thenReturn(materielExistant);

        // ACT + ASSERT — aucune exception ne doit être levée
        assertDoesNotThrow(() -> materielService.creerMateriel(dto, adminUtilisateur));

        // save() a bien été appelé
        verify(materielRepository, times(1)).save(any(Materiel.class));
    }

    @Test
    public void creerMateriel_avecNumSerieDejaExistant_devraitLeverMetierException() {
        // ARRANGE — le numéro de série est déjà en base
        MaterielRequestDto dto = new MaterielRequestDto("SN-001-TEST", 1, 1, null);

        when(materielRepository.findByNumSerie("SN-001-TEST")).thenReturn(Optional.of(materielExistant));

        // ACT + ASSERT
        MetierException exception = assertThrows(
                MetierException.class,
                () -> materielService.creerMateriel(dto, adminUtilisateur)
        );

        assertTrue(
                exception.getMessage().contains("SN-001-TEST"),
                "Le message doit mentionner le numéro de série en doublon"
        );

        // save() ne doit PAS avoir été appelé
        verify(materielRepository, never()).save(any());
    }

    @Test
    public void creerMateriel_avecModeleInexistant_devraitLeverException() {
        // ARRANGE
        MaterielRequestDto dto = new MaterielRequestDto("SN-003-NOUVEAU", 99, 1, null);

        when(materielRepository.findByNumSerie("SN-003-NOUVEAU")).thenReturn(Optional.empty());
        when(modeleRepository.findById(99)).thenReturn(Optional.empty()); // modèle introuvable

        // ACT + ASSERT
        assertThrows(
                RessourceIntrouvableException.class,
                () -> materielService.creerMateriel(dto, adminUtilisateur)
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // CHANGER LE STATUT
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void modifierStatutMateriel_avecStatutValide_devraitMettreAJour() {
        // ARRANGE
        StatutMateriel enPanne = new StatutMateriel();
        enPanne.setId(2);
        enPanne.setLibelle("En panne");

        when(materielRepository.findById(1)).thenReturn(Optional.of(materielExistant));
        when(statutMaterielRepository.findById(2)).thenReturn(Optional.of(enPanne));
        when(materielRepository.save(any())).thenReturn(materielExistant);

        // ACT + ASSERT
        assertDoesNotThrow(() -> materielService.modifierStatutMateriel(1, 2));

        verify(materielRepository, times(1)).save(any(Materiel.class));
    }

    @Test
    public void modifierStatutMateriel_avecMaterielInexistant_devraitLeverException() {
        // ARRANGE
        when(materielRepository.findById(99)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                RessourceIntrouvableException.class,
                () -> materielService.modifierStatutMateriel(99, 1)
        );
    }
}
