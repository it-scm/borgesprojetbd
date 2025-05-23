package com.gestionecole.service;

import com.gestionecole.model.AnneeSection;
import com.gestionecole.model.Section;
import com.gestionecole.repository.AnneeSectionRepository;
import com.gestionecole.repository.InscriptionRepository;
import com.gestionecole.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final AnneeSectionRepository anneeSectionRepository;
    private final InscriptionRepository inscriptionRepository;

    public SectionService(SectionRepository sectionRepository,
                          AnneeSectionRepository anneeSectionRepository,
                          InscriptionRepository inscriptionRepository) {
        this.sectionRepository = sectionRepository;
        this.anneeSectionRepository = anneeSectionRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    private String getCurrentAcademicYear() {
        LocalDate today = LocalDate.now();
        int year = today.getMonthValue() >= 9 ? today.getYear() : today.getYear() - 1;
        return year + "-" + (year + 1);
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
        String currentAcademicYear = getCurrentAcademicYear();

        for (Section section : sections) {
            Optional<AnneeSection> anneeSectionOpt = anneeSectionRepository
                .findByAnneeAcademiqueAndSection_Id(currentAcademicYear, section.getId());

            if (anneeSectionOpt.isPresent()) {
                long nbInscrits = inscriptionRepository.countByAnneeSectionId(anneeSectionOpt.get().getId());
                section.setPlacesRestantes((int) (section.getNbPlaces() - nbInscrits));
            } else {
                // If no AnneeSection for current year, assume all places are available
                section.setPlacesRestantes(section.getNbPlaces());
            }
        }
        return sections;
    }
}
