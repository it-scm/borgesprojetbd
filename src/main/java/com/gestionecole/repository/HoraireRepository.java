package com.gestionecole.repository;

import com.gestionecole.model.Horaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoraireRepository extends JpaRepository<Horaire, Long> {
    List<Horaire> findByCours_AnneeSection_Section_NomAndCours_AnneeSection_AnneeAcademique(String sectionNom, String anneeAcademique);
    List<Horaire> findByCours_AnneeSection_Id(Long anneeSectionId);
}
