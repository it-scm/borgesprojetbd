package com.gestionecole.service;

import com.gestionecole.model.*;
import com.gestionecole.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final SectionRepository sectionRepository;
    private final AnneeSectionRepository anneeSectionRepository;
    private final HoraireRepository horaireRepository;
    private final InscriptionRepository inscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    public EtudiantService(EtudiantRepository etudiantRepository,
                           SectionRepository sectionRepository,
                           AnneeSectionRepository anneeSectionRepository,
                           HoraireRepository horaireRepository,
                           InscriptionRepository inscriptionRepository,
                           PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.sectionRepository = sectionRepository;
        this.anneeSectionRepository = anneeSectionRepository;
        this.horaireRepository = horaireRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Optional<Etudiant> getEtudiantByEmail(String email) {
        return etudiantRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Etudiant> getEtudiantWithSectionByEmail(String email) {
        return etudiantRepository.findWithSectionByEmail(email);
    }


    public boolean isEtudiantInscrit(String email) {
        return etudiantRepository.findByEmail(email)
                .map(Etudiant::isInscrit)
                .orElse(false);
    }

    @Transactional
    public void registerStudent(Etudiant etudiant, Long sectionId) {
        // 1. Find section and check capacity
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalStateException("Section non trouvée"));

        int placesRestantes = section.getNbPlaces() - etudiantRepository.countBySectionId(sectionId);
        if (placesRestantes <= 0) {
            throw new IllegalStateException("Plus de places disponibles dans cette section.");
        }

        // 2. Encode password
        etudiant.setPassword(passwordEncoder.encode(etudiant.getPassword()));

        // 3. Assign role and section
        etudiant.setRole(Roles.ETUDIANT);
        etudiant.setSection(section);

        // 4. Generate and set matricule like E-00001
        etudiant.setMatricule(generateNextMatricule());

        // 5. Determine current academic year
        String academicYear = getCurrentAcademicYear();

        // 6. Fetch AnneeSection
        AnneeSection anneeSection = anneeSectionRepository
                .findByAnneeAcademiqueAndSection(academicYear, section)
                .orElseThrow(() -> new IllegalStateException("Année académique non trouvée pour la section " + section.getNom()));

        etudiant.setAnneeSection(anneeSection);

        // 7. Save Etudiant
        etudiantRepository.save(etudiant);

        // 8. Enroll in all courses for this year
        List<Horaire> horaires = horaireRepository.findByAnneeSectionId(anneeSection.getId());
        for (Horaire horaire : horaires) {
            Inscription inscription = new Inscription();
            inscription.setEtudiant(etudiant);
            inscription.setCours(horaire.getCours());
            inscription.setDateInscription(LocalDate.now());
            inscriptionRepository.save(inscription);
        }
    }


    private String getCurrentAcademicYear() {
        LocalDate today = LocalDate.now();
        int year = today.getMonthValue() >= 9 ? today.getYear() : today.getYear() - 1;
        return year + "-" + (year + 1);
    }

    private String generateNextMatricule() {
        long count = etudiantRepository.count() + 1;
        return String.format("E-%05d", count);
    }


    public Etudiant saveEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }

    public int countEtudiantsInSection(Long sectionId) {
        return etudiantRepository.countBySectionId(sectionId);
    }

    public void save(Etudiant existingEtudiant) {
        etudiantRepository.save(existingEtudiant);
    }
}
