package com.gestionecole.controller;

import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Professeur;
import com.gestionecole.model.Section;
import com.gestionecole.repository.*;
import jakarta.persistence.EntityManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager entityManager;

    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private CoursRepository coursRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private ProfesseurRepository professeurRepository;
    @Autowired private SectionRepository sectionRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private HoraireRepository horaireRepository;
    @Autowired private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void cleanUpDatabase() {
        entityManager.createNativeQuery("DELETE FROM note").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM inscription").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM horaire").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM cours").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM etudiant").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM annee_section").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM section").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM professeur").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM utilisateur").executeUpdate();

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
    void testListTables() {
        jdbcTemplate.query(
                "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'PUBLIC'",
                (rs) -> {
                    System.out.println("==== DATABASE TABLES ====");
                    while (rs.next()) {
                        System.out.println(rs.getString("table_name"));
                    }
                    System.out.println("=========================");
                }
        );
    }


    @Test
    void testRedirectionEtudiantInscrit() throws Exception {
        Section section = new Section("Cyber", 30);
        sectionRepository.save(section);

        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Etudiant");
        etudiant.setPrenom("Inscrit");
        etudiant.setEmail("etudiant.inscrit@ecole.be");
        etudiant.setPassword(passwordEncoder.encode("etudiant123"));
        etudiant.setRole("ROLE_ETUDIANT");
        etudiant.setSection(section);
        etudiantRepository.save(etudiant);

        mockMvc.perform(formLogin("/auth/login")
                        .user("etudiant.inscrit@ecole.be")
                        .password("etudiant123"))
                .andExpect(authenticated().withUsername("etudiant.inscrit@ecole.be"))
                .andExpect(redirectedUrl("/etudiant/cours"));
    }


    @Test
    void testRedirectionProfesseurAfterLogin() throws Exception {
        Professeur prof = new Professeur();
        prof.setNom("Jean");
        prof.setPrenom("Professeur");
        prof.setEmail("professeur@ecole.be");
        prof.setPassword(passwordEncoder.encode("professeur123"));
        prof.setRole("ROLE_PROFESSEUR");
        prof.setMatricule("P-10001"); // 🔥 Matricule is set

        professeurRepository.save(prof);  // 🔥 Then save child (Professeur)

        mockMvc.perform(formLogin("/auth/login")
                        .user("professeur@ecole.be")
                        .password("professeur123"))
                .andExpect(authenticated().withUsername("professeur@ecole.be"))
                .andExpect(redirectedUrl("/professeur/cours"));
    }


    @Test
    void testLienInscriptionPresentSurLoginPage() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("href=\"/auth/register\"")));
    }

    @Test
    void testLoginInvalideRedirigeVersErreur() throws Exception {
        mockMvc.perform(formLogin("/auth/login")
                        .user("email-inexistant@ecole.be")
                        .password("motdepasseincorrect"))
                .andExpect(redirectedUrl("/auth/login?error=true"));
    }

    @Test
    void testPageRegisterAccessible() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(content().string(Matchers.containsString("Inscription Étudiant")));
    }


    @Test
    void testRegisterEtudiantSuccess() throws Exception {
        // Arrange
        Section section = new Section("Informatique", 30);
        sectionRepository.save(section);

        // Act
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/auth/register")
                                .param("nom", "Martin")
                                .param("prenom", "Bob")
                                .param("email", "bob.martin@ecole.be")
                                .param("password", "Pass1234")
                                .param("sectionId", section.getId().toString())
                                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()) // 🔥 Needed for POST
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));

        // Assert: verify student is created
        Etudiant savedEtudiant = etudiantRepository.findByEmail("bob.martin@ecole.be").orElseThrow();
        assertTrue(passwordEncoder.matches("Pass1234", savedEtudiant.getPassword()));
        assertNotNull(savedEtudiant.getSection());
    }



    @Test
    void testPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Etudiant");
        etudiant.setPrenom("NonInscrit");
        etudiant.setEmail("bob.martin@ecole.be");
        etudiant.setPassword(encoder.encode("Pass1234"));
        etudiant.setMatricule("E-10009");
        etudiant.setRole("ROLE_ETUDIANT");
        etudiant.setSection(null);
        etudiantRepository.save(etudiant);

        String storedHash = etudiantRepository.findByEmail("bob.martin@ecole.be").orElseThrow().getPassword();
        assertTrue(encoder.matches("Pass1234", storedHash));
    }
    @Test
    void testPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Pass1234";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }



    @Test
    void showSchema() {
        jdbcTemplate.query(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'PROFESSEUR'",
                (rs) -> {
                    System.out.println("==== PROFESSEUR COLUMNS ====");
                    while (rs.next()) {
                        System.out.println(rs.getString("COLUMN_NAME"));
                    }
                    System.out.println("============================");
                }
        );
    }

}

