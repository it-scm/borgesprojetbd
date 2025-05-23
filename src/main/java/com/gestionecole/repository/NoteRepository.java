package com.gestionecole.repository;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByInscription_Etudiant(Etudiant etudiant); // Changed from findByEtudiant
    Optional<Note> findByInscription_IdAndCours_Id(Long inscriptionId, Long coursId); // Changed from findByEtudiant_IdAndCours_Id
    List<Note> findByCours(Cours cours);
    List<Note> findByInscription_Id(Long inscriptionId); // Added for convenience
}
