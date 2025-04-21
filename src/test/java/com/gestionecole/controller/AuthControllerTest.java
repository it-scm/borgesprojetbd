package com.gestionecole.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPageLoginAccessible() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(content().string(Matchers.containsString("form")));
    }

    @Test
    void testRedirectionEtudiantInscrit() throws Exception {
        mockMvc.perform(formLogin().user("etudiant.inscrit@ecole.be").password("password"))
                .andExpect(authenticated().withUsername("etudiant.inscrit@ecole.be"))
                .andExpect(redirectedUrl("/etudiant/cours"));
    }

    @Test
    void testRedirectionProfesseur() throws Exception {
        mockMvc.perform(formLogin().user("professeur@ecole.be").password("password"))
                .andExpect(authenticated().withUsername("professeur@ecole.be"))
                .andExpect(redirectedUrl("/professeur/cours"));
    }

    @Test
    void testLoginError() throws Exception {
        mockMvc.perform(formLogin().user("inconnu@ecole.be").password("incorrect"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void testLienInscriptionVisible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(content().string(Matchers.containsString("href=\"/register\"")));
    }
}
