package com.gestionecole.controller;

import com.gestionecole.service.EtudiantService;
import com.gestionecole.service.HoraireService;
import com.gestionecole.service.InscriptionService;
import com.gestionecole.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List; // Added import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final InscriptionService inscriptionService;
    private final HoraireService horaireService;
    private final NoteService noteService;

    public EtudiantController(EtudiantService etudiantService, InscriptionService inscriptionService, HoraireService horaireService, NoteService noteService) {
        this.etudiantService = etudiantService;
        this.inscriptionService = inscriptionService;
        this.horaireService = horaireService;
        this.noteService = noteService;
    }

    @GetMapping("/cours")
    public String voirCours(Model model) {
        String email = getCurrentUserEmail();
        etudiantService.getEtudiantByEmail(email).ifPresent(etudiant -> {
            model.addAttribute("inscriptions", inscriptionService.getInscriptionsByEtudiant(etudiant));
        });
        return "etudiant/cours";
    }

    @GetMapping("/horaire")
    public String voirHoraire(Model model) {
        String email = getCurrentUserEmail();
        etudiantService.getEtudiantByEmail(email).ifPresent(etudiant -> {
            List<com.gestionecole.model.Inscription> inscriptions = inscriptionService.getInscriptionsByEtudiant(etudiant);
            if (!inscriptions.isEmpty()) {
                // Assuming the first inscription is the relevant one for the current context.
                // More sophisticated logic might be needed to pick the "current" AnneeSection if a student has multiple.
                com.gestionecole.model.AnneeSection anneeSection = inscriptions.get(0).getAnneeSection();
                if (anneeSection != null && anneeSection.getSection() != null) {
                    model.addAttribute("horaires", horaireService.getHoraireBySectionAndAnnee(
                            anneeSection.getSection().getNom(),
                            anneeSection.getAnneeAcademique()
                    ));
                } else {
                    model.addAttribute("horaires", java.util.Collections.emptyList());
                }
            } else {
                model.addAttribute("horaires", java.util.Collections.emptyList()); // No inscriptions, so no horaires
            }
        });
        return "etudiant/horaire";
    }


    @GetMapping("/note")
    public String voirNotes(Model model) {
        String email = getCurrentUserEmail();
        etudiantService.getEtudiantByEmail(email).ifPresent(etudiant -> {
            model.addAttribute("notes", noteService.getNotesByEtudiant(etudiant));
        });
        return "etudiant/note";
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
