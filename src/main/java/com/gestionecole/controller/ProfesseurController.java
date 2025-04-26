package com.gestionecole.controller;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Note;
import com.gestionecole.model.Professeur;
import com.gestionecole.service.CoursService;
import com.gestionecole.service.HoraireService;
import com.gestionecole.service.NoteService;
import com.gestionecole.service.ProfesseurService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/professeur")
public class ProfesseurController {

    private final ProfesseurService professeurService;
    private final CoursService coursService;
    private final HoraireService horaireService;
    private final NoteService noteService;

    public ProfesseurController(ProfesseurService professeurService, CoursService coursService, HoraireService horaireService, NoteService noteService) {
        this.professeurService = professeurService;
        this.coursService = coursService;
        this.horaireService = horaireService;
        this.noteService = noteService;
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
            model.addAttribute("cours", cours);
            model.addAttribute("etudiants", noteService.getEtudiantsInscritsAuCours(cours));
        }
        return "professeur/notes/liste_etudiants";
    }

    @GetMapping("/notes/modifier/{etudiantId}/{coursId}")
    public String modifierNoteForm(@PathVariable Long etudiantId, @PathVariable Long coursId, Model model) {
        Note note = noteService.getNoteByEtudiantAndCours(etudiantId, coursId).orElseGet(() -> {
            Note nouvelleNote = new Note();
            Etudiant etudiant = new Etudiant();
            etudiant.setId(etudiantId);
            Cours cours = new Cours();
            cours.setId(coursId);
            nouvelleNote.setEtudiant(etudiant);
            nouvelleNote.setCours(cours);
            return nouvelleNote;
        });
        model.addAttribute("note", note);
        model.addAttribute("coursId", coursId);
        return "professeur/notes/modifier";
    }

    @PostMapping("/notes/modifier")
    public String enregistrerNote(@RequestParam("etudiantId") Long etudiantId,
                                  @RequestParam("coursId") Long coursId,
                                  @RequestParam(value = "premiereSession", required = false) Double premiereSession,
                                  @RequestParam(value = "deuxiemeSession", required = false) Double deuxiemeSession,
                                  Model model) {
        Note existingNote = noteService.getNoteByEtudiantAndCours(etudiantId, coursId).orElse(new Note());
        Etudiant etudiant = new Etudiant();
        etudiant.setId(etudiantId);
        Cours cours = new Cours();
        cours.setId(coursId);
        existingNote.setEtudiant(etudiant);
        existingNote.setCours(cours);

        if (premiereSession != null) {
            existingNote.setPremiereSession(premiereSession);
        }
        if (deuxiemeSession != null) {
            if (existingNote.getPremiereSession() == null) {
                model.addAttribute("errorMessage", "La note de deuxième session ne peut être ajoutée tant que la première session n'a pas eu lieu.");
                model.addAttribute("note", existingNote);
                model.addAttribute("coursId", coursId);
                return "professeur/notes/modifier";
            }
            existingNote.setDeuxiemeSession(deuxiemeSession);
        }

        noteService.saveNote(existingNote);
        return "redirect:/professeur/notes/" + coursId;
    }
}