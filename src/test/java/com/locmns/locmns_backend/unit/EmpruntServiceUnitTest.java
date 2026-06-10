package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.constants.Role;
import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.mapper.EmpruntMapper;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.*;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.services.IEmpruntService;
import com.locmns.locmns_backend.validator.emprunt.EmpruntValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmpruntServiceUnitTest {

    private EmpruntRepository empruntRepository;
    private MaterielRepository materielRepository;
    private UtilisateurRepository utilisateurRepository;
    private StatutEmpruntRepository statutEmpruntRepository;
    private EmpruntMapper empruntMapper;

    private IEmpruntService empruntService;

    private Materiel materielDisponible;
    private Utilisateur utilisateur;
    private Utilisateur admin;
    private StatutEmprunt statutEmprunt;
    private EmpruntRequestDto dto;

    @BeforeEach
    void init() {

        empruntRepository = mock(EmpruntRepository.class);
        materielRepository = mock(MaterielRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        statutEmpruntRepository = mock(StatutEmpruntRepository.class);
        empruntMapper = mock(EmpruntMapper.class);

        EmpruntValidator empruntValidator = new EmpruntValidator();

        empruntService = new EmpruntService(
                empruntRepository,
                materielRepository,
                utilisateurRepository,
                statutEmpruntRepository,
                empruntValidator,
                empruntMapper
        );

        CategorieModele categorieModele = new CategorieModele();
        categorieModele.setId(1);

        Modele modele = new Modele();
        modele.setCategorieModele(categorieModele);

        materielDisponible = new Materiel();
        materielDisponible.setId(1);
        materielDisponible.setModele(modele);

        CategorieUtilisateur categorieUtilisateur = new CategorieUtilisateur();
        categorieUtilisateur.setCode("UTILISATEUR");

        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setCategorieUtilisateur(categorieUtilisateur);

        CategorieUtilisateur categorieAdmin = new CategorieUtilisateur();
        categorieAdmin.setCode(Role.ADMIN.name());

        admin = new Utilisateur();
        admin.setId(999);
        admin.setCategorieUtilisateur(categorieAdmin);

        statutEmprunt = new StatutEmprunt();
        statutEmprunt.setId(3);

        dto = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                null,
                ""
        );

        Emprunt emprunt = new Emprunt();

        EmpruntResponseDto response = mock(EmpruntResponseDto.class);

        when(empruntMapper.toEntity(any()))
                .thenReturn(emprunt);

        when(empruntMapper.toDto(any()))
                .thenReturn(response);

        when(empruntRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(statutEmpruntRepository.findByCode("DEMANDE_EN_COURS"))
                .thenReturn(Optional.of(statutEmprunt));
    }

    @Test
    void creerEmpruntAvecDonneesValides_devraitReussir() {

        when(materielRepository.findById(1))
                .thenReturn(Optional.of(materielDisponible));

        when(materielRepository
                .retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                        any(),
                        any(),
                        eq(1)))
                .thenReturn(List.of(materielDisponible));

        assertDoesNotThrow(() ->
                empruntService.creerEmprunt(dto, utilisateur));

        verify(empruntRepository).save(any(Emprunt.class));
    }

    @Test
    void creerEmpruntAvecDateFinAvantDateDebut_devraitLeverMetierException() {

        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now().plusDays(7),
                LocalDate.now(),
                1,
                null,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide, utilisateur)
        );

        assertEquals(
                "La date de fin doit être après la date de début",
                exception.getMessage()
        );
    }

    @Test
    void creerEmpruntAvecDateDebutDansLePasse_devraitLeverMetierException() {

        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(7),
                1,
                null,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide, utilisateur)
        );

        assertEquals(
                "La date de début ne peut pas être dans le passé",
                exception.getMessage()
        );
    }

    @Test
    void creerEmpruntAvecMaterielInexistant_devraitLeverMetierException() {

        when(materielRepository.findById(99))
                .thenReturn(Optional.empty());

        EmpruntRequestDto dtoInvalide = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                99,
                null,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoInvalide, utilisateur)
        );

        assertEquals(
                "Matériel introuvable",
                exception.getMessage()
        );
    }

    @Test
    void creerEmpruntAvecMaterielIndisponible_devraitLeverMetierException() {

        when(materielRepository.findById(1))
                .thenReturn(Optional.of(materielDisponible));

        when(materielRepository
                .retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                        any(),
                        any(),
                        eq(1)))
                .thenReturn(List.of());

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dto, utilisateur)
        );

        assertEquals(
                "Matériel indisponible sur la période demandée",
                exception.getMessage()
        );
    }

    @Test
    void creerEmpruntAdminSansUtilisateurCible_devraitLeverMetierException() {

        EmpruntRequestDto dtoAdmin = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                null,
                ""
        );

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoAdmin, admin)
        );

        assertEquals(
                "Un admin doit spécifier l'utilisateur cible",
                exception.getMessage()
        );
    }

    @Test
    void creerEmpruntAdminAvecUtilisateurInexistant_devraitLeverMetierException() {

        EmpruntRequestDto dtoAdmin = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                999,
                ""
        );

        when(utilisateurRepository.findById(999))
                .thenReturn(Optional.empty());

        MetierException exception = assertThrows(
                MetierException.class,
                () -> empruntService.creerEmprunt(dtoAdmin, admin)
        );

        assertEquals(
                "Utilisateur introuvable",
                exception.getMessage()
        );
    }

    @Test
    void creerEmpruntAdminAvecUtilisateurValide_devraitReussir() {

        EmpruntRequestDto dtoAdmin = new EmpruntRequestDto(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                1,
                1,
                ""
        );

        when(utilisateurRepository.findById(1))
                .thenReturn(Optional.of(utilisateur));

        when(materielRepository.findById(1))
                .thenReturn(Optional.of(materielDisponible));

        when(materielRepository
                .retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
                        any(),
                        any(),
                        eq(1)))
                .thenReturn(List.of(materielDisponible));

        assertDoesNotThrow(() ->
                empruntService.creerEmprunt(dtoAdmin, admin));

        verify(empruntRepository).save(any(Emprunt.class));
    }
}