package com.gestionecole.controller;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Professeur;
import com.gestionecole.repository.CoursRepository;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.ProfesseurRepository;
import com.gestionecole.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;


    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private CoursRepository coursRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private ProfesseurRepository professeurRepository;

    @BeforeAll
    void setupUsers() {
        // Supprimer d'abord les dépendances (cours) pour respecter les contraintes de clé étrangère
        coursRepository.deleteAll();
        etudiantRepository.deleteAll();
        professeurRepository.deleteAll();

        // Création du professeur
        Professeur prof = new Professeur();
        prof.setNom("Jean");
        prof.setPrenom("Professeur");
        prof.setEmail("professeur@ecole.be");
        prof.setPassword(passwordEncoder.encode("professeur123"));
        prof.setRole("ROLE_PROFESSEUR");
        prof.setMatricule("ABC123");
        professeurRepository.save(prof);

        // Création de cours associés au professeur
        Cours cours1 = new Cours("CYB101", "Introduction à la cybersécurité", 5, "Principes de base", prof);
        Cours cours2 = new Cours("TEL202", "Télécoms avancés", 6, "Réseaux avancés", prof);
        coursRepository.saveAll(List.of(cours1, cours2));

        // Création de l'étudiant
        Etudiant etu = new Etudiant();
        etu.setNom("Etudiant");
        etu.setPrenom("Inscrit");
        etu.setEmail("etudiant.inscrit@ecole.be");
        etu.setPassword(passwordEncoder.encode("etudiant123"));
        etu.setRole("ROLE_ETUDIANT");
        etudiantRepository.save(etu);
    }



    @Test
    void testJpaContext() {
        assertNotNull(entityManager);
    }


    @Test
    void testPageLoginAccessible() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(content().string(Matchers.containsString("form")));
    }



    @Test
    void testRedirectionEtudiantInscrit() throws Exception {
        System.out.println(new BCryptPasswordEncoder().encode("etudiant123"));
        mockMvc.perform(formLogin("/auth/login").user("etudiant.inscrit@ecole.be").password("etudiant123"))
                .andExpect(authenticated().withUsername("etudiant.inscrit@ecole.be"))
                .andExpect(redirectedUrl("/etudiant/cours"));

    }

    @Test
    void testRedirectionProfesseurAfterLogin() throws Exception {
        // Juste pour vérifier que l'utilisateur existe
        assertTrue(utilisateurRepository.findByEmail("professeur@ecole.be").isPresent());

        // Authentification avec les identifiants du professeur
        mockMvc.perform(formLogin("/auth/login")
                        .user("professeur@ecole.be")
                        .password("professeur123"))
                .andExpect(authenticated().withUsername("professeur@ecole.be"))
                .andExpect(redirectedUrl("/professeur/cours"));
    }


    @Test
    void testRedirectionEtudiantInscrit2() throws Exception {
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
