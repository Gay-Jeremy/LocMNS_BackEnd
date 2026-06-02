package com.locmns.locmns_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MaterielEtUtilisateurIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // Sert à sérialiser les objets Java en JSON pour les requêtes POST
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // ════════════════════════════════════════════════════════════════════════
    // SÉCURITÉ — vérifier que les endpoints sont protégés
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void appelListeMateriels_sansAuthentification_devraitRetourner403() throws Exception {
        // Aucun utilisateur connecté → Spring Security doit bloquer la requête
        mvc.perform(get("/api/materiels/liste"))
                .andExpect(status().isForbidden()); // 403 Forbidden
    }

    @Test
    public void appelListeMaterielsDisponibles_sansAuthentification_devraitRetourner403() throws Exception {
        mvc.perform(get("/api/materiels/liste/disponible"))
                .andExpect(status().isForbidden());
    }

    // ════════════════════════════════════════════════════════════════════════
    // MATÉRIELS — endpoints accessibles avec un utilisateur connecté
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @WithMockUser(roles = "Étudiant") // Simule un utilisateur connecté avec le rôle par défaut
    public void appelListeMaterielsDisponibles_avecUtilisateurConnecte_devraitRetourner200() throws Exception {
        mvc.perform(get("/api/materiels/liste/disponible"))
                .andExpect(status().isOk())              // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); // réponse en JSON
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void appelListeMateriels_devraitRetournerUnTableauJSON() throws Exception {
        mvc.perform(get("/api/materiels/liste/disponible"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()); // $ = racine, isArray() = c'est bien un tableau
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void appelMaterielParId_avecIdInexistant_devraitRetourner404() throws Exception {
        mvc.perform(get("/api/materiels/9999")) // id qui n'existe sûrement pas
                .andExpect(status().isNotFound()); // 404 Not Found
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILISATEURS
    // ════════════════════════════════════════════════════════════════════════

    @Test
    @WithMockUser(roles = "Étudiant")
    public void appelListeUtilisateurs_devraitRetourner200() throws Exception {
        // Note : l'endpoint /api/utilisateurs/liste n'a pas @IsUser dans ce projet
        // → accessible sans authentification
        mvc.perform(get("/api/utilisateurs/liste"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void appelListeUtilisateurs_devraitRetournerUnTableauJSON() throws Exception {
        mvc.perform(get("/api/utilisateurs/liste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void appelUtilisateurParId_avecIdInexistant_devraitRetourner404() throws Exception {
        mvc.perform(get("/api/utilisateurs/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void creerUtilisateur_avecCorpsVide_devraitRetourner400() throws Exception {
        // POST sans body → validation Spring doit retourner 400 Bad Request
        mvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // corps vide (champs obligatoires manquants)
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void creerUtilisateur_avecEmailMalFormate_devraitRetourner400() throws Exception {
        // On envoie un email invalide → Spring Validation doit rejeter
        String corpsInvalide = """
                {
                    "nom": "Test",
                    "prenom": "Utilisateur",
                    "email": "pasunEmail",
                    "password": "mdp123",
                    "categorieUtilisateurId": 1
                }
                """;

        mvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpsInvalide))
                .andExpect(status().isBadRequest());
    }
}