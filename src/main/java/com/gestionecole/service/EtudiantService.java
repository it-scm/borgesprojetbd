package com.gestionecole.service;

import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Section;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.SectionRepository;
import com.gestionecole.model.Roles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final SectionRepository sectionRepository;
    private final PasswordEncoder passwordEncoder;

    public EtudiantService(EtudiantRepository etudiantRepository, SectionRepository sectionRepository, PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.sectionRepository = sectionRepository;
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

    public boolean isEtudiantInscrit(String email) {
        return etudiantRepository.findByEmail(email)
                .map(Etudiant::isInscrit)
                .orElse(false);
    }

    @Transactional
    public void registerStudent(Etudiant etudiant, Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalStateException("Section non trouvée"));

        int placesRestantes = section.getNbPlaces() - etudiantRepository.countBySectionId(sectionId);
        if (placesRestantes <= 0) {
            throw new IllegalStateException("Plus de places disponibles dans cette section.");
        }

        etudiant.setPassword(passwordEncoder.encode(etudiant.getPassword()));
        etudiant.setSection(section);
        etudiant.setRole(Roles.ETUDIANT);
        etudiantRepository.save(etudiant);
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
}
