package com.locmns.locmns_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
    public void appelListeEmprunts_sansAuthentification_devraitRetourner403() throws Exception {
        mvc.perform(get("/api/emprunts/liste"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
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