package com.gestionecole.service;

import com.gestionecole.model.*;
import com.gestionecole.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EtudiantServiceTest {

    @Autowired private EtudiantService etudiantService;
    @Autowired private SectionRepository sectionRepository;
    @Autowired private AnneeSectionRepository anneeSectionRepository;
    @Autowired private HoraireRepository horaireRepository;
    @Autowired private CoursRepository coursRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private InscriptionRepository inscriptionRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterStudentCreatesEtudiantMatriculeAnneeAndInscriptions() {
        // Arrange
        Section section = new Section("Informatique", 25);
        sectionRepository.save(section);

        String academicYear = "2024-2025";

        AnneeSection anneeSection = new AnneeSection(academicYear, section);
        anneeSectionRepository.save(anneeSection);

        Cours cours = new Cours();
        cours.setIntitule("Java avancé");
        cours.setAnneeSection(anneeSection); // Link Cours to AnneeSection
        coursRepository.save(cours);

        // Horaire is linked to Cours, not directly to AnneeSection anymore
        // The test setup for Horaire might not be strictly necessary for testing student registration's
        // automatic inscription to an AnneeSection, but if it were for testing course-specific notes via Horaire,
        // it would need to be linked to the 'cours' that is part of 'anneeSection'.
        // For now, the Horaire setup related to automatic course enrollment via Horaire in EtudiantService
        // was commented out/removed, so this specific Horaire setup might be less relevant for this test's core assertions.
        // Horaire horaire = new Horaire();
        // horaire.setJour("LUNDI");
        // horaire.setHeureDebut("08:00");
        // horaire.setHeureFin("10:00");
        // horaire.setCours(cours);
        // horaireRepository.save(horaire);

        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Alice");
        etudiant.setPrenom("Durand");
        etudiant.setEmail("alice.durand@ecole.be");
        etudiant.setPassword("pass123");

        // Act
        etudiantService.registerStudent(etudiant, section.getId(), academicYear); // Pass academicYear

        // Assert
        Etudiant saved = etudiantRepository.findByEmail("alice.durand@ecole.be").orElseThrow();

        assertThat(saved.getMatricule()).matches("E-\\d{5}");
        assertThat(passwordEncoder.matches("pass123", saved.getPassword())).isTrue();

        List<Inscription> inscriptions = inscriptionRepository.findByEtudiant(saved);
        assertThat(inscriptions).hasSize(1); // Student should have one inscription
        Inscription studentInscription = inscriptions.get(0);
        assertThat(studentInscription.getAnneeSection()).isEqualTo(anneeSection);
        assertThat(studentInscription.getAnneeSection().getSection()).isEqualTo(section);
        assertThat(studentInscription.getAnneeSection().getAnneeAcademique()).isEqualTo(academicYear);

        // The automatic course enrollment logic in EtudiantService.registerStudent was removed/commented out.
        // That logic previously created Inscription records for each Cours in the Horaire for the AnneeSection.
        // Now, an Etudiant is simply inscribed to an AnneeSection.
        // To verify the student is "enrolled" in "Java avancé", we'd check if "Java avancé"
        // is one of the courses offered in the 'anneeSection' they are inscribed in.
        // For this test, we already linked 'cours' ("Java avancé") to 'anneeSection'.
        // So, the student is inscribed to an AnneeSection that contains this course.
        boolean isCourseInAnneeSection = coursRepository.findByAnneeSection_Id(anneeSection.getId()) // Corrected method name
                                            .stream()
                                            .anyMatch(c -> c.getIntitule().equals("Java avancé"));
        assertThat(isCourseInAnneeSection).isTrue();
    }
}
