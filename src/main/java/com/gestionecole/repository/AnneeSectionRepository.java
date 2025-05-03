package com.gestionecole.repository;

import com.gestionecole.model.AnneeSection;
import com.gestionecole.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnneeSectionRepository extends JpaRepository<AnneeSection, Long> {
    Optional<AnneeSection> findByAnneeAcademiqueAndSection(String s, Section section);
}