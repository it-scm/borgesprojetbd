package com.gestionecole.repository;

import com.gestionecole.model.AnneeSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnneeSectionRepository extends JpaRepository<AnneeSection, Long> {
}