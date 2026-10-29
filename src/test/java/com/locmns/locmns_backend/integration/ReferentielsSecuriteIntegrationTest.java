package com.locmns.locmns_backend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Référentiels (catégories, modèles, salles, statuts, droits d'emprunt...) :
 * tout utilisateur connecté peut les lire, seul un administrateur peut les modifier.
 *
 * Les corps envoyés sont valides : Spring valide le corps avant de vérifier le rôle,
 * un corps invalide renverrait 400 et masquerait l'absence de contrôle d'accès.
 */
@SpringBootTest
class ReferentielsSecuriteIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private static final String LIBELLE = """
            { "libelle": "Test" }
            """;
    private static final String LIBELLE_ET_CODE = """
            { "libelle": "Test", "code": "TEST" }
            """;
    private static final String COMPOSANT = """
            { "libelle": "Test", "categorieModeleIds": [1] }
            """;
    private static final String DOCUMENTATION = """
            { "titre": "Test", "url": "https://exemple.test/doc", "format": "PDF", "typeDocumentId": 1, "modeleIds": [1] }
            """;
    private static final String MODELE = """
            { "libelle": "Test", "categorieModeleId": 1 }
            """;
    private static final String DROIT_EMPRUNT = """
            { "categorieUtilisateurId": 3, "categorieModeleId": 1, "peutEmprunter": true }
            """;

    /** Toutes les routes d'écriture des référentiels, sur des données existantes (id 1). */
    static Stream<Arguments> routesEcriture() {
        return Stream.of(
                Arguments.of("POST", "/api/categories_modele", LIBELLE),
                Arguments.of("PUT", "/api/categories_modele/1", LIBELLE),
                Arguments.of("DELETE", "/api/categories_modele/1", null),
                Arguments.of("POST", "/api/categories_utilisateur", LIBELLE_ET_CODE),
                Arguments.of("PUT", "/api/categories_utilisateur/1", LIBELLE_ET_CODE),
                Arguments.of("DELETE", "/api/categories_utilisateur/1", null),
                Arguments.of("POST", "/api/composants", COMPOSANT),
                Arguments.of("PUT", "/api/composants/1", COMPOSANT),
                Arguments.of("DELETE", "/api/composants/1", null),
                Arguments.of("POST", "/api/documentations", DOCUMENTATION),
                Arguments.of("PUT", "/api/documentations/1", DOCUMENTATION),
                Arguments.of("DELETE", "/api/documentations/1", null),
                Arguments.of("POST", "/api/droits_emprunt", DROIT_EMPRUNT),
                Arguments.of("DELETE", "/api/droits_emprunt/1/1", null),
                Arguments.of("POST", "/api/modeles", MODELE),
                Arguments.of("PUT", "/api/modeles/1", MODELE),
                Arguments.of("DELETE", "/api/modeles/1", null),
                Arguments.of("POST", "/api/promotions", LIBELLE),
                Arguments.of("PUT", "/api/promotions/1", LIBELLE),
                Arguments.of("DELETE", "/api/promotions/1", null),
                Arguments.of("POST", "/api/salles", LIBELLE),
                Arguments.of("PUT", "/api/salles/1", LIBELLE),
                Arguments.of("DELETE", "/api/salles/1", null),
                Arguments.of("POST", "/api/statuts_emprunt", LIBELLE_ET_CODE),
                Arguments.of("PUT", "/api/statuts_emprunt/1", LIBELLE_ET_CODE),
                Arguments.of("DELETE", "/api/statuts_emprunt/1", null),
                Arguments.of("POST", "/api/statuts_materiel", LIBELLE_ET_CODE),
                Arguments.of("PUT", "/api/statuts_materiel/1", LIBELLE_ET_CODE),
                Arguments.of("DELETE", "/api/statuts_materiel/1", null),
                Arguments.of("POST", "/api/types_document", LIBELLE),
                Arguments.of("PUT", "/api/types_document/1", LIBELLE),
                Arguments.of("DELETE", "/api/types_document/1", null)
        );
    }

    /**
     * Routes d'écriture côté administrateur, sans effet sur le jeu d'essai partagé :
     * les créations ajoutent des lignes, les modifications et suppressions visent un id inexistant.
     */
    static Stream<Arguments> routesEcritureSansEffetDeBord() {
        return routesEcriture().map(a -> {
            Object[] args = a.get();
            String url = (String) args[1];
            if (!args[0].equals("POST")) {
                url = url.endsWith("/1/1")
                        ? url.replaceFirst("/1/1$", "/99999/99999")
                        : url.replaceFirst("/1$", "/99999");
            }
            return Arguments.of(args[0], url, args[2]);
        });
    }

    static Stream<String> routesLecture() {
        return Stream.of("categories_modele", "categories_utilisateur", "composants", "documentations",
                        "droits_emprunt", "modeles", "promotions", "salles", "statuts_emprunt",
                        "statuts_materiel", "types_document")
                .map(ressource -> "/api/" + ressource + "/liste");
    }

    private ResultActions envoyer(String methode, String url, String corps)
            throws Exception {
        var requete = request(HttpMethod.valueOf(methode), url);
        if (corps != null) {
            requete.contentType(MediaType.APPLICATION_JSON).content(corps);
        }
        return mvc.perform(requete);
    }

    // ════════════════════════════════════════════════════════════════════════
    // ÉCRITURE — refusée aux étudiants et aux enseignants
    // ════════════════════════════════════════════════════════════════════════

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("routesEcriture")
    @WithMockUser(roles = "Étudiant")
    void ecritureReferentiel_parUnEtudiant_devraitRetourner403(String methode, String url, String corps)
            throws Exception {
        envoyer(methode, url, corps).andExpect(status().isForbidden());
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("routesEcriture")
    @WithMockUser(roles = "Enseignant")
    void ecritureReferentiel_parUnEnseignant_devraitRetourner403(String methode, String url, String corps)
            throws Exception {
        envoyer(methode, url, corps).andExpect(status().isForbidden());
    }

    // ════════════════════════════════════════════════════════════════════════
    // ÉCRITURE — autorisée à l'administrateur
    // ════════════════════════════════════════════════════════════════════════

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("routesEcritureSansEffetDeBord")
    @WithMockUser(roles = "Administrateur")
    void ecritureReferentiel_parUnAdministrateur_nestPasRefusee(String methode, String url, String corps)
            throws Exception {
        // 201, 204 ou 404 (id inexistant) selon la route : seul compte l'absence de refus d'accès
        envoyer(methode, url, corps).andExpect(result ->
                assertNotEquals(403, result.getResponse().getStatus(), "accès refusé à l'administrateur"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // LECTURE — ouverte à tout utilisateur connecté (utilisée par le frontend)
    // ════════════════════════════════════════════════════════════════════════

    @ParameterizedTest(name = "GET {0}")
    @MethodSource("routesLecture")
    @WithMockUser(roles = "Étudiant")
    void lectureReferentiel_parUnEtudiant_devraitRetourner200(String url) throws Exception {
        mvc.perform(get(url)).andExpect(status().isOk());
    }

    @ParameterizedTest(name = "GET {0}")
    @MethodSource("routesLecture")
    void lectureReferentiel_sansAuthentification_devraitRetourner401(String url) throws Exception {
        mvc.perform(get(url)).andExpect(status().isUnauthorized());
    }
}
