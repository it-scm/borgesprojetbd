package com.gestionecole.controller;

import com.gestionecole.model.*; // Import all models
import com.gestionecole.service.*; // Import all services
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professeur")
public class ProfesseurController {

    private final ProfesseurService professeurService;
    private final CoursService coursService;
    private final HoraireService horaireService;
    private final NoteService noteService;
    private final InscriptionService inscriptionService; // Added

    public ProfesseurController(ProfesseurService professeurService,
                                CoursService coursService,
                                HoraireService horaireService,
                                NoteService noteService,
                                InscriptionService inscriptionService) { // Added
        this.professeurService = professeurService;
        this.coursService = coursService;
        this.horaireService = horaireService;
        this.noteService = noteService;
        this.inscriptionService = inscriptionService; // Added
    }

    @GetMapping("/cours")
    public String voirCours(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Professeur professeur = professeurService.getProfesseurByEmail(email).orElse(null);
        if (professeur != null) {
            model.addAttribute("cours", coursService.getCoursByProfesseur(professeur));
        }
        return "professeur/cours/liste";
    }


    @GetMapping("/horaires")
    public String voirHoraires(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Professeur professeur = professeurService.getProfesseurByEmail(email).orElse(null);
        if (professeur != null) {
            model.addAttribute("horaires", horaireService.getHorairesByProfesseur(professeur));
        }
        return "professeur/horaires/liste";
    }

    @GetMapping("/notes")
    public String listeCoursNotes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Professeur professeur = professeurService.getProfesseurByEmail(email).orElse(null);
        if (professeur != null) {
            model.addAttribute("cours", coursService.getCoursByProfesseur(professeur));
        }
        return "professeur/notes/liste_cours";
    }


    @GetMapping("/notes/{coursId}")
    public String listeEtudiantsPourCours(@PathVariable Long coursId, Model model) {
        Cours cours = coursService.getCoursById(coursId).orElse(null);
        if (cours != null) {
            List<Etudiant> etudiants = noteService.getEtudiantsInscritsAuCours(cours); // This service method was updated
            Map<Long, Note> notes = new HashMap<>();
            for (Etudiant etudiant : etudiants) {
                // Use the new service method that takes Etudiant and Cours objects
                noteService.getNoteByEtudiantAndCours(etudiant, cours)
                        .ifPresent(note -> notes.put(etudiant.getId(), note));
            }
            model.addAttribute("cours", cours);
            model.addAttribute("etudiants", etudiants);
            model.addAttribute("notes", notes);
        }
        return "professeur/notes/liste_etudiants";
    }


    @GetMapping("/notes/modifier/{etudiantId}/{coursId}")
    public String modifierNoteForm(@PathVariable Long etudiantId, @PathVariable Long coursId, Model model) {
        Etudiant etudiant = noteService.findEtudiantById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid etudiant Id:" + etudiantId));
        Cours cours = coursService.getCoursById(coursId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cours Id:" + coursId));

        Note note = noteService.getNoteByEtudiantAndCours(etudiant, cours).orElseGet(() -> {
            Note nouvelleNote = new Note();
            // We pass Etudiant and Cours to the model for the form to use,
            // the Inscription will be resolved on POST.
            // For display, we can set them directly on a transient Note if needed by the form,
            // but the Note object sent to createOrUpdateNote must have Inscription.
            // The form should primarily submit etudiantId, coursId, and note values.
            nouvelleNote.setCours(cours); // For form display
            // Create a placeholder Inscription with the Etudiant for form display
            Inscription placeholderInscription = new Inscription();
            placeholderInscription.setEtudiant(etudiant);
            nouvelleNote.setInscription(placeholderInscription); // For form display (note.inscription.etudiant.nom)
            return nouvelleNote;
        });

        model.addAttribute("note", note); // note.inscription.etudiant will be available if note exists
        model.addAttribute("etudiant", etudiant); // Explicitly add etudiant for form if note is new
        model.addAttribute("cours", cours); // Explicitly add cours for form
        // The form should submit etudiantId and coursId along with note values.
        return "professeur/notes/modifier";
    }

    @PostMapping("/notes/modifier")
    public String enregistrerNote(@RequestParam Long etudiantId,
                                  @RequestParam Long coursId,
                                  @RequestParam(required = false) Double premiereSession,
                                  @RequestParam(required = false) Double deuxiemeSession,
                                  RedirectAttributes redirectAttributes) {

        Etudiant etudiant = noteService.findEtudiantById(etudiantId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid etudiant Id:" + etudiantId));
        Cours cours = coursService.getCoursById(coursId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid cours Id:" + coursId));

        if (cours.getAnneeSection() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le cours n'est associé à aucune année de section.");
            return "redirect:/professeur/notes/" + coursId;
        }

        // InscriptionService is now directly injected
        Inscription inscription = inscriptionService.getInscriptionByEtudiantAndAnneeSection(etudiant, cours.getAnneeSection())
            .orElseThrow(() -> {
                // Or handle this by creating an inscription if that's the business logic
                redirectAttributes.addFlashAttribute("errorMessage", "L'étudiant n'est pas inscrit à l'année/section de ce cours.");
                return new IllegalStateException("Inscription non trouvée pour l'étudiant et l'année/section du cours.");
            });


        Note noteToSave = new Note();
        noteToSave.setInscription(inscription);
        noteToSave.setCours(cours);
        noteToSave.setPremiereSession(premiereSession);
        noteToSave.setDeuxiemeSession(deuxiemeSession);

        try {
            // createOrUpdateNote will findExisting or use this new one
            noteService.createOrUpdateNote(noteToSave);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/professeur/notes/" + coursId;
        }

        return "redirect:/professeur/notes/" + coursId;
    }

}