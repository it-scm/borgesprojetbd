package com.gestionecole.service;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Inscription;
import com.gestionecole.model.Note;
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

    public NoteService(NoteRepository noteRepository, InscriptionService inscriptionService) {
        this.noteRepository = noteRepository;
        this.inscriptionService = inscriptionService;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> getNotesByEtudiant(Etudiant etudiant) {
        return noteRepository.findByEtudiant(etudiant);
    }

    public Optional<Note> getNoteByEtudiantAndCours(Long etudiantId, Long coursId) {
        return noteRepository.findByEtudiant_IdAndCours_Id(etudiantId, coursId);
    }

    public List<Note> getNotesByCours(Cours cours) {
        return noteRepository.findByCours(cours);
    }

    @Transactional
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Etudiant> getEtudiantsInscritsAuCours(Cours cours) {
        return inscriptionService.getInscriptionsByCours(cours)
                .stream()
                .map(Inscription::getEtudiant)
                .collect(Collectors.toList());
    }
}