package com.gestionecole.config;

import com.gestionecole.model.*;
import com.gestionecole.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final PasswordEncoder passwordEncoder;
    private final ProfesseurRepository professeurRepository;
    private final EtudiantRepository etudiantRepository;
    private final SectionRepository sectionRepository;
    private final CoursRepository coursRepository;
    private final AnneeSectionRepository anneeSectionRepository;
    private final HoraireRepository horaireRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            try {
                initProfesseurs();
                initSections();
                initAnneeSections();
                initCours();
                initHoraires();
                logger.info("✅ Base de données initialisée");
            } catch (Exception e) {
                logger.error("Erreur lors de l'initialisation de la base de données : {}", e.getMessage(), e);
            }
        };
    }

    private void initProfesseurs() {
        if (professeurRepository.count() == 0) {
            List<Professeur> professeurs = Arrays.asList(
                    createProfesseur("Bernair", "Michel", "michel.bernair@ecole.be", "PROFESSEUR", passwordEncoder.encode("Pass1234")),
                    createProfesseur("Hecquet", "Jean-Paul", "jean-paul.hecquet@ecole.be", "PROFESSEUR", passwordEncoder.encode("Pass1234")),
                    createProfesseur("Jaghou", "Ali", "ali.jaghou@ecole.be", "PROFESSEUR", passwordEncoder.encode("Pass1234")),
                    createProfesseur("Lemaire", "David", "david.lemaire@ecole.be", "PROFESSEUR", passwordEncoder.encode("Pass1234"))
            );
            professeurRepository.saveAll(professeurs);
            logger.info("✅ Professeurs initiaux insérés.");
        } else {
            logger.info("❌ La table Professeur est déjà initialisée.");
        }
    }

    private Professeur createProfesseur(String nom, String prenom, String email, String role, String password) {
        Professeur p = new Professeur();
        p.setNom(nom);
        p.setPrenom(prenom);
        p.setEmail(email);
        p.setPassword(password);
        p.setRole(role);
        p.setMatricule("R-" + System.currentTimeMillis()); // ✅ Valeur auto pour le matricule
        return p;
    }

    private void initSections() {
        if (sectionRepository.count() == 0) {
            sectionRepository.saveAll(Arrays.asList(
                    createSection("Télécom", 30),
                    createSection("Cyber", 25),
                    createSection("Électronique", 20)
            ));
            logger.info("✅ Sections insérées.");
        } else {
            logger.info("❌ La table Section est déjà initialisée.");
        }
    }

    private Section createSection(String nom, int nbPlaces) {
        Section s = new Section();
        s.setNom(nom);
        s.setNbPlaces(nbPlaces);
        return s;
    }

    private void initAnneeSections() {
        if (anneeSectionRepository.count() == 0 && sectionRepository.count() > 0) {
            sectionRepository.findAll().forEach(section -> {
                AnneeSection as = new AnneeSection();
                as.setAnneeAcademique("2024-2025");
                as.setSection(section);
                anneeSectionRepository.save(as);
            });
            logger.info("✅ AnneeSections insérées.");
        } else {
            logger.info("❌ La table AnneeSection est déjà initialisée.");
        }
    }

    private void initCours() {
        if (coursRepository.count() == 0 && professeurRepository.count() > 0) {
            Professeur professeur = professeurRepository.findAll().get(0); // par défaut
            coursRepository.saveAll(Arrays.asList(
                    createCours("UE299", "Bases des réseaux", 5, professeur),
                    createCours("UE301", "Bases de données", 4, professeur),
                    createCours("UE302", "Analyse informatique", 4, professeur)
            ));
            logger.info("✅ Cours insérés.");
        } else {
            logger.info("❌ La table Cours est déjà initialisée.");
        }
    }

    private Cours createCours(String code, String intitule, int credits, Professeur professeur) {
        Cours c = new Cours();
        c.setCode(code);
        c.setIntitule(intitule);
        c.setCredits(credits);
        c.setProfesseur(professeur);
        return c;
    }

    private void initHoraires() {
        if (horaireRepository.count() == 0 && coursRepository.count() > 0 && anneeSectionRepository.count() > 0) {
            Cours cours = coursRepository.findAll().get(0);
            AnneeSection anneeSection = anneeSectionRepository.findAll().get(0);

            Horaire h = new Horaire();
            h.setJour("Lundi");
            h.setHeureDebut("08h00");
            h.setHeureFin("10h00");
            h.setCours(cours);
            h.setAnneeSection(anneeSection);
            horaireRepository.save(h);
            logger.info("✅ Horaire inséré.");
        } else {
            logger.info("❌ La table Horaire est déjà initialisée.");
        }
    }
}
