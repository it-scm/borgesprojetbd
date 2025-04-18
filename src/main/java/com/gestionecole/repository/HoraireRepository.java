package com.gestionecole.repository;

import com.gestionecole.model.Horaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoraireRepository extends JpaRepository<Horaire, Long> {
    List<Horaire> findByAnneeSection_Section_NomAndAnneeSection_AnneeAcademique(String sectionNom, String anneeAcademique);
}
