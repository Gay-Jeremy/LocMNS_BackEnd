package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.CategorieUtilisateur;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.repository.CategorieUtilisateurRepository;
import com.locmns.locmns_backend.repository.PromotionRepository;
import com.locmns.locmns_backend.repository.UtilisateurRepository;
import com.locmns.locmns_backend.services.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UtilisateurServiceUnitTest {

    // ── MOCKS (faux objets créés par Mockito) ────────────────────────────────
    private UtilisateurRepository utilisateurRepository;
    private CategorieUtilisateurRepository categorieUtilisateurRepository;
    private PromotionRepository promotionRepository;
    private PasswordEncoder passwordEncoder;

    // Le vrai service qu'on teste, alimenté avec les mocks
    private UtilisateurService utilisateurService;

    // ── DONNÉES COMMUNES ─────────────────────────────────────────────────────
    private Utilisateur utilisateurExistant;
    private CategorieUtilisateur categorieAdmin;


    @BeforeEach
    public void init() {
        // Mockito.mock() crée un faux objet qui ne fait rien par défaut
        utilisateurRepository          = Mockito.mock(UtilisateurRepository.class);
        categorieUtilisateurRepository = Mockito.mock(CategorieUtilisateurRepository.class);
        promotionRepository            = Mockito.mock(PromotionRepository.class);
        passwordEncoder                = Mockito.mock(PasswordEncoder.class);

        // On injecte les mocks dans le vrai service
        utilisateurService = new UtilisateurService(
                passwordEncoder,
                utilisateurRepository,
                categorieUtilisateurRepository,
                promotionRepository
        );

        // Catégorie ADMIN réutilisée dans plusieurs tests
        categorieAdmin = new CategorieUtilisateur();
        categorieAdmin.setId(1);
        categorieAdmin.setLibelle("Administrateur");
        categorieAdmin.setCode("ADMIN");

        // Utilisateur fictif qui "existe déjà" en base
        utilisateurExistant = new Utilisateur();
        utilisateurExistant.setId(1);
        utilisateurExistant.setNom("Dupont");
        utilisateurExistant.setPrenom("Jean");
        utilisateurExistant.setEmail("jean.dupont@locmns.fr");
        utilisateurExistant.setCategorieUtilisateur(categorieAdmin);
    }

    // ════════════════════════════════════════════════════════════════════════
    // LISTER LES UTILISATEURS
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void obtenirTousLesUtilisateurs_devraitRetournerLaListe() {
        // ARRANGE — le faux repository retourne 1 utilisateur
        when(utilisateurRepository.findAll()).thenReturn(List.of(utilisateurExistant));

        // ACT
        List<UtilisateurResponseDto> resultat = utilisateurService.obtenirTousLesUtilisateurs();

        // ASSERT
        assertEquals(1, resultat.size(), "La liste doit contenir 1 utilisateur");
    }

    @Test
    public void obtenirTousLesUtilisateurs_quandAucun_devraitRetournerListeVide() {
        // ARRANGE — aucun utilisateur en base
        when(utilisateurRepository.findAll()).thenReturn(List.of());

        // ACT
        List<UtilisateurResponseDto> resultat = utilisateurService.obtenirTousLesUtilisateurs();

        // ASSERT
        assertTrue(resultat.isEmpty(), "La liste doit être vide");
    }

    // ════════════════════════════════════════════════════════════════════════
    // OBTENIR PAR ID
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void obtenirUtilisateurParId_quandExiste_devraitRetournerLUtilisateur() {
        // ARRANGE
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateurExistant));

        // ACT
        Optional<UtilisateurResponseDto> resultat = utilisateurService.obtenirUtilisateurParId(1);

        // ASSERT
        assertTrue(resultat.isPresent(), "Un utilisateur doit être trouvé");
        assertEquals("Dupont", resultat.get().nom());
    }

    @Test
    public void obtenirUtilisateurParId_quandInexistant_devraitRetournerVide() {
        // ARRANGE — findById retourne vide pour un id inexistant
        when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());

        // ACT
        Optional<UtilisateurResponseDto> resultat = utilisateurService.obtenirUtilisateurParId(99);

        // ASSERT
        assertFalse(resultat.isPresent(), "Aucun utilisateur ne doit être trouvé");
    }

    // ════════════════════════════════════════════════════════════════════════
    // CRÉER UN UTILISATEUR
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void creerUtilisateur_avecDonneesValides_devraitAppelerSave() {
        // ARRANGE
        UtilisateurRequestDto dto = new UtilisateurRequestDto(
                "Martin", "Sophie", "sophie.martin@locmns.fr",
                "motdepasse123", null, 1
        );

        when(categorieUtilisateurRepository.findById(1)).thenReturn(Optional.of(categorieAdmin));
        when(passwordEncoder.encode("motdepasse123")).thenReturn("$2a$HASH");

        // On configure ce que save() retourne
        Utilisateur sauvegarde = new Utilisateur();
        sauvegarde.setId(2);
        sauvegarde.setNom("Martin");
        sauvegarde.setPrenom("Sophie");
        sauvegarde.setEmail("sophie.martin@locmns.fr");
        sauvegarde.setCategorieUtilisateur(categorieAdmin);
        when(utilisateurRepository.save(any())).thenReturn(sauvegarde);

        // ACT
        UtilisateurResponseDto resultat = utilisateurService.creerUtilisateur(dto);

        // ASSERT — save() a bien été appelé
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
        assertEquals("Martin", resultat.nom());
    }

    @Test
    public void creerUtilisateur_avecCategorieInexistante_devraitLeverException() {
        // ARRANGE — la catégorie n'existe pas
        UtilisateurRequestDto dto = new UtilisateurRequestDto(
                "Martin", "Sophie", "sophie@locmns.fr",
                "mdp", null, 99 // id catégorie inexistant
        );
        when(categorieUtilisateurRepository.findById(99)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                RessourceIntrouvableException.class,
                () -> utilisateurService.creerUtilisateur(dto),
                "Doit lever une exception si la catégorie n'existe pas"
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // SUPPRIMER UN UTILISATEUR
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void supprimerUtilisateur_quandExiste_devraitAppelerDeleteById() {
        // ARRANGE
        when(utilisateurRepository.existsById(1)).thenReturn(true);

        // ACT
        utilisateurService.supprimerUtilisateur(1);

        // ASSERT — deleteById a bien été appelé avec l'id 1
        verify(utilisateurRepository, times(1)).deleteById(1);
    }

    @Test
    public void supprimerUtilisateur_quandInexistant_devraitLeverException() {
        // ARRANGE
        when(utilisateurRepository.existsById(99)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                RessourceIntrouvableException.class,
                () -> utilisateurService.supprimerUtilisateur(99),
                "Doit lever une exception si l'utilisateur n'existe pas"
        );

        // On vérifie que deleteById n'a PAS été appelé (pas de suppression)
        verify(utilisateurRepository, never()).deleteById(any());
    }
}