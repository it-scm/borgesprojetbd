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
    // private final SectionRepository sectionRepository; // Removed as Etudiant no longer directly holds Section
    // private final AnneeSectionRepository anneeSectionRepository; // Removed as Etudiant no longer directly holds AnneeSection
    private final HoraireRepository horaireRepository; // May still be needed for other logic, review later
    private final InscriptionRepository inscriptionRepository;
    private final SectionRepository sectionRepository; // Keep for section related operations like capacity check
    private final AnneeSectionRepository anneeSectionRepository; // Keep for AnneeSection related operations
    private final PasswordEncoder passwordEncoder;

    public EtudiantService(EtudiantRepository etudiantRepository,
                           SectionRepository sectionRepository, // Keep for section related operations
                           AnneeSectionRepository anneeSectionRepository, // Keep for AnneeSection related operations
                           HoraireRepository horaireRepository,
                           InscriptionRepository inscriptionRepository,
                           PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.sectionRepository = sectionRepository; // Keep for section related operations
        this.anneeSectionRepository = anneeSectionRepository; // Keep for AnneeSection related operations
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
    public Optional<Etudiant> getEtudiantWithDetailsByEmail(String email) { // Renamed, direct section is gone
        // Implementation needs to change. Fetch Etudiant, then fetch Inscriptions, then AnneeSections, then Section.
        // This is a complex query now, might need specific repository method or multiple queries.
        // For now, just returning the Etudiant. Details can be fetched separately.
        return etudiantRepository.findByEmail(email);
    }


    public boolean isEtudiantInscrit(String email) {
        // Logic to check if an etudiant has any active inscription.
        // This could be for the current academic year, for example.
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(email);
        if (etudiantOpt.isPresent()) {
            // Assuming an etudiant is "inscrit" if they have at least one inscription record.
            // More sophisticated logic might be needed (e.g., check inscription for current academic year).
            return !inscriptionRepository.findByEtudiantId(etudiantOpt.get().getId()).isEmpty();
        }
        return false;
    }

    @Transactional
    public void registerStudent(Etudiant etudiant, Long sectionId, String anneeAcademique) {
        // 1. Find section
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalStateException("Section non trouvée"));

        // 2. Find AnneeSection
        AnneeSection anneeSection = anneeSectionRepository
            .findByAnneeAcademiqueAndSection_Id(anneeAcademique, sectionId) // Corrected method name
            .orElseThrow(() -> new IllegalStateException("Année académique '" + anneeAcademique + "' non trouvée pour la section '" + section.getNom() + "'"));

        // 3. Check capacity for AnneeSection (count existing inscriptions for this anneeSection)
        long inscriptionsCount = inscriptionRepository.countByAnneeSectionId(anneeSection.getId());
        if (inscriptionsCount >= section.getNbPlaces()) {
            throw new IllegalStateException("Plus de places disponibles dans cette section pour l'année " + anneeAcademique);
        }

        // 4. Encode password
        etudiant.setPassword(passwordEncoder.encode(etudiant.getPassword()));

        // 5. Assign role
        etudiant.setRole(Roles.ETUDIANT);

        // 6. Generate and set matricule like E-00001
        etudiant.setMatricule(generateNextMatricule());

        // 7. Save Etudiant first to get an ID
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        // 8. Create Inscription record
        Inscription inscription = new Inscription();
        inscription.setEtudiant(savedEtudiant);
        inscription.setAnneeSection(anneeSection);
        inscription.setDateInscription(LocalDate.now());
        inscriptionRepository.save(inscription);

        // Automatic enrollment in courses associated with AnneeSection can be handled here if needed
        // For example, by iterating through Cours linked to AnneeSection and creating default Note entries or other related records.
        // This part is removed for now as the original logic for Horaire-based enrollment is no longer directly applicable
        // List<Cours> coursesForAnneeSection = coursRepository.findByAnneeSectionId(anneeSection.getId());
        // for (Cours cours : coursesForAnneeSection) {
        //    // Potentially create default records related to each course for the student
        // }
    }


    public String getCurrentAcademicYear() { // This might be better placed in a utility class or config
        LocalDate today = LocalDate.now();
        int year = today.getMonthValue() >= 9 ? today.getYear() : today.getYear() - 1;
        return year + "-" + (year + 1);
    }

    private String generateNextMatricule() {
        long count = etudiantRepository.count() + 1; // Consider a more robust sequence generation for matricule
        return String.format("E-%05d", count);
    }


    public Etudiant saveEtudiant(Etudiant etudiant) {
        // If password is provided and not encoded, encode it.
        // This is a basic check; more robust logic might be needed depending on update scenarios.
        if (etudiant.getPassword() != null && !etudiant.getPassword().startsWith("$2a$")) { // Basic check for encoded password
            etudiant.setPassword(passwordEncoder.encode(etudiant.getPassword()));
        }
        return etudiantRepository.save(etudiant);
    }

    public void deleteEtudiant(Long id) {
        // Consider cascading deletes or checks for related entities (e.g., Inscription, Note)
        // For now, just deleting the etudiant. Dependent records might cause FK violations if not handled.
        inscriptionRepository.deleteAll(inscriptionRepository.findByEtudiantId(id)); // Delete related inscriptions
        // Notes are linked to Inscription, so they should be handled by cascade or explicit delete if Inscription is deleted.
        etudiantRepository.deleteById(id);
    }

    public long countEtudiantsByAnneeSection(Long anneeSectionId) {
        return inscriptionRepository.countByAnneeSectionId(anneeSectionId);
    }

    // This method might be redundant if saveEtudiant handles both create and update.
    public void save(Etudiant existingEtudiant) {
        etudiantRepository.save(existingEtudiant);
    }
}
