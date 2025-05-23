package com.gestionecole.repository;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByEtudiant(Etudiant etudiant);
    boolean existsByEtudiantAndCours(Etudiant etudiant, Cours cours);
    List<Inscription> findByCours(Cours cours);
}