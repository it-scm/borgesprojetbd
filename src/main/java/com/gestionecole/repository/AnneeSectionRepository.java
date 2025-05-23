package com.gestionecole.repository;

import com.gestionecole.model.AnneeSection;
import com.gestionecole.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnneeSectionRepository extends JpaRepository<AnneeSection, Long> {
    Optional<AnneeSection> findByAnneeAcademiqueAndSection(String anneeAcademique, Section section);
    Optional<AnneeSection> findByAnneeAcademiqueAndSection_Id(String anneeAcademique, Long sectionId); // Changed method name to follow convention
}