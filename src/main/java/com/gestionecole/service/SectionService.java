package com.gestionecole.service;

import com.gestionecole.model.Section;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final EtudiantRepository etudiantRepository;

    public SectionService(SectionRepository sectionRepository, EtudiantRepository etudiantRepository) {
        this.sectionRepository = sectionRepository;
        this.etudiantRepository = etudiantRepository;
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Optional<Section> getSectionById(Long id) {
        return sectionRepository.findById(id);
    }

    public Optional<Section> getSectionByNom(String nom) {
        return sectionRepository.findByNom(nom);
    }

    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    public List<Section> findAllSectionsWithRemainingPlaces() {
        List<Section> sections = sectionRepository.findAll();
        for (Section section : sections) {
            int nbInscrits = etudiantRepository.countBySectionId(section.getId());
            section.setPlacesRestantes(section.getNbPlaces() - nbInscrits);
        }
        return sections;
    }
}
