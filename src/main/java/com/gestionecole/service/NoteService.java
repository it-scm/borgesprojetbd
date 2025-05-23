package com.gestionecole.service;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Inscription;
import com.gestionecole.model.Note;
import com.gestionecole.repository.CoursRepository;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final InscriptionService inscriptionService;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;

    public NoteService(NoteRepository noteRepository, InscriptionService inscriptionService, EtudiantRepository etudiantRepository, CoursRepository coursRepository) {
        this.noteRepository = noteRepository;
        this.inscriptionService = inscriptionService;
        this.etudiantRepository = etudiantRepository;
        this.coursRepository = coursRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> getNotesByEtudiant(Etudiant etudiant) {
        return noteRepository.findByInscription_Etudiant(etudiant);
    }

    // New method to find Note by Etudiant and Cours, abstracting away Inscription lookup for the controller
    public Optional<Note> getNoteByEtudiantAndCours(Etudiant etudiant, Cours cours) {
        if (etudiant == null || cours == null || cours.getAnneeSection() == null) {
            return Optional.empty();
        }
        Optional<Inscription> inscriptionOpt = inscriptionService.getInscriptionByEtudiantAndAnneeSection(etudiant, cours.getAnneeSection());
        if (inscriptionOpt.isPresent()) {
            return noteRepository.findByInscription_IdAndCours_Id(inscriptionOpt.get().getId(), cours.getId());
        }
        return Optional.empty();
    }

    // Existing method, useful when inscriptionId is known
    public Optional<Note> getNoteByInscriptionAndCours(Long inscriptionId, Long coursId) {
        return noteRepository.findByInscription_IdAndCours_Id(inscriptionId, coursId);
    }

    public List<Note> getNotesByInscriptionId(Long inscriptionId) {
        return noteRepository.findByInscription_Id(inscriptionId);
    }

    public List<Note> getNotesByCours(Cours cours) {
        return noteRepository.findByCours(cours);
    }

    @Transactional
    public void saveNote(Note note) {
        noteRepository.saveAndFlush(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Etudiant> getEtudiantsInscritsAuCours(Cours cours) {
        if (cours.getAnneeSection() == null) {
            return List.of(); // Or throw an exception, a course should belong to an AnneeSection
        }
        return inscriptionService.getInscriptionsByAnneeSection(cours.getAnneeSection())
                .stream()
                .map(Inscription::getEtudiant)
                .distinct() // Avoid duplicate students if multiple inscriptions for same student in an anneeSection (should not happen with current model)
                .collect(Collectors.toList());
    }

    public Optional<Etudiant> findEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Optional<Cours> findCoursById(Long id) {
        return coursRepository.findById(id);
    }

    @Transactional
    public void createOrUpdateNote(Note note) {
        if (note.getInscription() == null || note.getInscription().getId() == null) {
            throw new IllegalArgumentException("Inscription ID must be provided to create or update a note.");
        }
        if (note.getCours() == null || note.getCours().getId() == null) {
            throw new IllegalArgumentException("Cours ID must be provided to create or update a note.");
        }

        Long inscriptionId = note.getInscription().getId();
        Long coursId = note.getCours().getId();

        Optional<Note> existingNoteOpt = noteRepository.findByInscription_IdAndCours_Id(inscriptionId, coursId);

        Note noteToSave = existingNoteOpt.orElseGet(() -> {
            Note newNote = new Note();
            newNote.setInscription(note.getInscription());
            newNote.setCours(note.getCours());
            return newNote;
        });

        // 🔐 Business rule: Deuxième session requires première session
        if (note.getDeuxiemeSession() != null &&
                (noteToSave.getPremiereSession() == null && note.getPremiereSession() == null)) {
            throw new IllegalStateException("Impossible d’ajouter une note en deuxième session sans note en première session.");
        }

        // Update sessions if provided
        if (note.getPremiereSession() != null) {
            noteToSave.setPremiereSession(note.getPremiereSession());
        }
        if (note.getDeuxiemeSession() != null) {
            noteToSave.setDeuxiemeSession(note.getDeuxiemeSession());
        }

        noteRepository.save(noteToSave);
    }

}