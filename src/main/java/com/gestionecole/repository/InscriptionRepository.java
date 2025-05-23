package com.gestionecole.repository;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Added import

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByEtudiant(Etudiant etudiant);
    List<Inscription> findByEtudiantId(Long etudiantId); // Added for EtudiantService
    // boolean existsByEtudiantAndCours(Etudiant etudiant, Cours cours); // Removed, logic to be handled in service
    List<Inscription> findByAnneeSection(com.gestionecole.model.AnneeSection anneeSection); // Changed from findByCours
    Optional<Inscription> findByEtudiantAndAnneeSection(Etudiant etudiant, com.gestionecole.model.AnneeSection anneeSection); // Added for InscriptionService
    long countByAnneeSectionId(Long anneeSectionId); // Added for EtudiantService
}