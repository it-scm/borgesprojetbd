package com.gestionecole.service;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Inscription;
import com.gestionecole.repository.InscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;

    public InscriptionService(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public Optional<Inscription> getInscriptionById(Long id) {
        return inscriptionRepository.findById(id);
    }

    public List<Inscription> getInscriptionsByEtudiant(Etudiant etudiant) {
        return inscriptionRepository.findByEtudiant(etudiant);
    }

    public List<Inscription> getInscriptionsByAnneeSection(com.gestionecole.model.AnneeSection anneeSection) {
        return inscriptionRepository.findByAnneeSection(anneeSection);
    }

    public Optional<Inscription> getInscriptionByEtudiantAndAnneeSection(Etudiant etudiant, com.gestionecole.model.AnneeSection anneeSection) {
        if (etudiant == null || anneeSection == null) {
            return Optional.empty();
        }
        // Assuming a student has at most one inscription per AnneeSection.
        // If multiple are possible, this logic needs refinement (e.g., find active one).
        return inscriptionRepository.findByEtudiantAndAnneeSection(etudiant, anneeSection);
    }

    // Replaced isEtudiantInscritAuCours with isEtudiantEnrolledInCourse
    // This new method will check if the student is inscribed in an AnneeSection that contains the given course.
    public boolean isEtudiantEnrolledInCourse(Etudiant etudiant, Cours cours) {
        List<Inscription> inscriptions = inscriptionRepository.findByEtudiant(etudiant);
        if (cours.getAnneeSection() == null) { // Course must be associated with an AnneeSection
            return false;
        }
        for (Inscription inscription : inscriptions) {
            if (inscription.getAnneeSection().equals(cours.getAnneeSection())) {
                return true; // Student is in an AnneeSection that matches the course's AnneeSection
            }
        }
        return false;
    }

    @Transactional
    public Inscription saveInscription(Inscription inscription) {
        return inscriptionRepository.save(inscription);
    }

    public void deleteInscription(Long id) {
        inscriptionRepository.deleteById(id);
    }
}