package com.gestionecole.service;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Note;
import com.gestionecole.repository.CoursRepository;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.NoteRepository;
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

    private Etudiant etudiant;
    private Cours cours;

    @BeforeEach
    void setup() {
        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
        etudiant.setEmail("john.doe@ecole.be");
        etudiant.setPassword("secret");
        etudiant.setRole("ROLE_ETUDIANT");
        etudiantRepository.save(etudiant);

        cours = new Cours();
        cours.setIntitule("Programmation");
        coursRepository.save(cours);
    }

    @Test
    void testCreateNoteWithPremiereSession() {
        Note note = new Note();
        note.setEtudiant(etudiant);
        note.setCours(cours);
        note.setPremiereSession(14.0);

        noteService.createOrUpdateNote(note);

        Note saved = noteRepository.findByEtudiant_IdAndCours_Id(etudiant.getId(), cours.getId()).orElseThrow();
        assertThat(saved.getPremiereSession()).isEqualTo(14.0);
        assertThat(saved.getDeuxiemeSession()).isNull();
    }

    @Test
    void testCreateNoteWithDeuxiemeSessionOnlyShouldFail() {
        Note note = new Note();
        note.setEtudiant(etudiant);
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
        note.setEtudiant(etudiant);
        note.setCours(cours);
        note.setPremiereSession(11.0);
        noteService.createOrUpdateNote(note);

        // Now, update with deuxième session
        Note update = new Note();
        update.setEtudiant(etudiant);
        update.setCours(cours);
        update.setDeuxiemeSession(13.0);  // only set deuxième
        noteService.createOrUpdateNote(update);

        Note saved = noteRepository.findByEtudiant_IdAndCours_Id(etudiant.getId(), cours.getId()).orElseThrow();
        assertThat(saved.getPremiereSession()).isEqualTo(11.0);
        assertThat(saved.getDeuxiemeSession()).isEqualTo(13.0);
    }

    @Test
    void testUpdatePremiereSessionWithoutAffectingDeuxieme() {
        // Create full note
        Note note = new Note();
        note.setEtudiant(etudiant);
        note.setCours(cours);
        note.setPremiereSession(12.0);
        note.setDeuxiemeSession(14.0);
        noteService.createOrUpdateNote(note);

        // Update only première
        Note update = new Note();
        update.setEtudiant(etudiant);
        update.setCours(cours);
        update.setPremiereSession(15.0);
        noteService.createOrUpdateNote(update);

        Note saved = noteRepository.findByEtudiant_IdAndCours_Id(etudiant.getId(), cours.getId()).orElseThrow();
        assertThat(saved.getPremiereSession()).isEqualTo(15.0);
        assertThat(saved.getDeuxiemeSession()).isEqualTo(14.0);
    }
}
