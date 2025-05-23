package com.gestionecole.service;

import com.gestionecole.model.Cours;
import com.gestionecole.model.*; // Import all models
import com.gestionecole.repository.*; // Import all repositories
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NoteServiceTest {

    @Autowired private NoteService noteService;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private CoursRepository coursRepository;
    @Autowired private NoteRepository noteRepository;
    @Autowired private SectionRepository sectionRepository; // Added
    @Autowired private AnneeSectionRepository anneeSectionRepository; // Added
    @Autowired private InscriptionRepository inscriptionRepository; // Added

    private Etudiant etudiant;
    private Cours cours;
    private Inscription inscription; // Added
    private AnneeSection anneeSection; // Added
    private Section section; // Added

    @BeforeEach
    void setup() {
        section = new Section("Test Section", 10);
        sectionRepository.save(section);

        anneeSection = new AnneeSection("2024-2025", section);
        anneeSectionRepository.save(anneeSection);

        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
        etudiant.setEmail("john.doe@ecole.be");
        etudiant.setPassword("secret");
        etudiant.setRole(Roles.ETUDIANT);
        etudiantRepository.save(etudiant);

        inscription = new Inscription();
        inscription.setEtudiant(etudiant);
        inscription.setAnneeSection(anneeSection);
        inscription.setDateInscription(java.time.LocalDate.now());
        inscriptionRepository.save(inscription);

        cours = new Cours();
        cours.setIntitule("Programmation");
        cours.setAnneeSection(anneeSection); // Link course to anneeSection
        coursRepository.save(cours);
    }

    @Test
    void testCreateNoteWithPremiereSession() {
        Note note = new Note();
        note.setInscription(inscription); // Use inscription
        note.setCours(cours);
        note.setPremiereSession(14.0);

        noteService.createOrUpdateNote(note);

        Note saved = noteRepository.findByInscription_IdAndCours_Id(inscription.getId(), cours.getId()).orElseThrow();
        assertThat(saved.getPremiereSession()).isEqualTo(14.0);
        assertThat(saved.getDeuxiemeSession()).isNull();
    }

    @Test
    void testCreateNoteWithDeuxiemeSessionOnlyShouldFail() {
        Note note = new Note();
        note.setInscription(inscription); // Use inscription
        note.setCours(cours);
        note.setPremiereSession(null);
        note.setDeuxiemeSession(12.5);

        assertThatThrownBy(() -> noteService.createOrUpdateNote(note))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("deuxième session");
    }

    @Test
    void testAddDeuxiemeSessionAfterPremiereSessionExists() {
        // First, create the note with première session
        Note note = new Note();
        note.setInscription(inscription); // Use inscription
        note.setCours(cours);
        note.setPremiereSession(11.0);
        noteService.createOrUpdateNote(note);

        // Now, update with deuxième session
        // createOrUpdateNote expects a full Note object with Inscription and Cours for lookup
        Note update = new Note();
        update.setInscription(inscription); // Use inscription
        update.setCours(cours);
        update.setDeuxiemeSession(13.0);  // only set deuxième
        noteService.createOrUpdateNote(update);

        Note saved = noteRepository.findByInscription_IdAndCours_Id(inscription.getId(), cours.getId()).orElseThrow();
        assertThat(saved.getPremiereSession()).isEqualTo(11.0);
        assertThat(saved.getDeuxiemeSession()).isEqualTo(13.0);
    }

    @Test
    void testUpdatePremiereSessionWithoutAffectingDeuxieme() {
        // Create full note
        Note note = new Note();
        note.setInscription(inscription); // Use inscription
        note.setCours(cours);
        note.setPremiereSession(12.0);
        note.setDeuxiemeSession(14.0);
        noteService.createOrUpdateNote(note);

        // Update only première
        Note update = new Note();
        update.setInscription(inscription); // Use inscription
        update.setCours(cours);
        update.setPremiereSession(15.0);
        noteService.createOrUpdateNote(update);

        Note saved = noteRepository.findByInscription_IdAndCours_Id(inscription.getId(), cours.getId()).orElseThrow();
        assertThat(saved.getPremiereSession()).isEqualTo(15.0);
        assertThat(saved.getDeuxiemeSession()).isEqualTo(14.0);
    }
}
