package com.gestionecole.repository;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    List<Cours> findByProfesseur(Professeur professeur);
    List<Cours> findByAnneeSection_Id(Long anneeSectionId); // Added method
}
