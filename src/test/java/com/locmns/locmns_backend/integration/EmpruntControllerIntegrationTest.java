package com.locmns.locmns_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class EmpruntControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(
        value = "jean.dupont@entreprise.fr",
        userDetailsServiceBeanName = "utilisateurDetailsService")
    void creerEmprunt_avecMaterielDisponible_devraitRetournerCreated() throws Exception {

        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.of(2027, 6, 1),
                LocalDate.of(2027, 6, 10),
                1,
                1,
                "Test création"
        );

        mvc.perform(post("/api/emprunts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.materiel.id").value(1))
                .andExpect(jsonPath("$.utilisateur.id").value(1));
    }

    @Test
    @WithUserDetails(
            value = "jean.dupont@entreprise.fr",
            userDetailsServiceBeanName = "utilisateurDetailsService")
    void creerEmprunt_materielIndisponible_devraitRetournerConflict() throws Exception {

        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.of(2027, 6, 1),
                LocalDate.of(2027, 6, 10),
                3,
                1,
                "Test conflit"
        );

        mvc.perform(post("/api/emprunts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void creerEmprunt_sansAuthentification_devraitRetournerIsUnauthorized() throws Exception {

        EmpruntRequestDto dto = new EmpruntRequestDto(
                LocalDate.of(2027, 6, 1),
                LocalDate.of(2027, 6, 10),
                1,
                1,
                "Test 401"
        );

        mvc.perform(post("/api/emprunts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void appelListeEmprunts_sansAuthentification_devraitRetourner401() throws Exception {
        mvc.perform(get("/api/emprunts/liste"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "Étudiant")
    public void appelListeEmprunts_avecUtilisateurConnecte_devraitRetourner200() throws Exception {
        mvc.perform(get("/api/emprunts/liste"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void appelListeEmprunts_devraitNePasExposerLeMotDePasse() throws Exception {
        mvc.perform(get("/api/emprunts/liste"))
                .andExpect(jsonPath("$[0].utilisateur.password").doesNotExist());
    }

}