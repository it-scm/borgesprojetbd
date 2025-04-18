package com.gestionecole.service;

import com.gestionecole.model.AnneeSection;
import com.gestionecole.repository.AnneeSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnneeSectionService {

    private final AnneeSectionRepository anneeSectionRepository;

    public AnneeSectionService(AnneeSectionRepository anneeSectionRepository) {
        this.anneeSectionRepository = anneeSectionRepository;
    }

    public List<AnneeSection> getAllAnneeSections() {
        return anneeSectionRepository.findAll();
    }

    public Optional<AnneeSection> getAnneeSectionById(Long id) {
        return anneeSectionRepository.findById(id);
    }

    public AnneeSection saveAnneeSection(AnneeSection anneeSection) {
        return anneeSectionRepository.save(anneeSection);
    }

    public void deleteAnneeSection(Long id) {
        anneeSectionRepository.deleteById(id);
    }
}