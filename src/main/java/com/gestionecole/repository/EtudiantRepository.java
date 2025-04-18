package com.gestionecole.repository;

import com.gestionecole.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmail(String email);
    int countBySectionId(Long sectionId); // Utilisé pour calculer les places restantes
}
