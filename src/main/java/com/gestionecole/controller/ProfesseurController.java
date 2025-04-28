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
            List<Etudiant> etudiants = noteService.getEtudiantsInscritsAuCours(cours);
            Map<Long, Note> notes = new HashMap<>();
            for (Etudiant etudiant : etudiants) {
                noteService.getNoteByEtudiantAndCours(etudiant.getId(), cours.getId())
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
        Note note = noteService.getNoteByEtudiantAndCours(etudiantId, coursId).orElseGet(() -> {
            Note nouvelleNote = new Note();
            Etudiant etudiant = new Etudiant();
            etudiant.setId(etudiantId);
            etudiant.setNom("Etudiant inconnu"); // 🔥 Placeholder value to avoid Thymeleaf error
            etudiant.setPrenom("");              // 🔥
            Cours cours = new Cours();
            cours.setId(coursId);
            cours.setIntitule("Cours inconnu");  // 🔥 Placeholder too
            nouvelleNote.setEtudiant(etudiant);
            nouvelleNote.setCours(cours);
            return nouvelleNote;
        });

        model.addAttribute("note", note);
        model.addAttribute("coursId", coursId);
        return "professeur/notes/modifier";
    }

    @PostMapping("/notes/modifier")
    public String enregistrerNote(@ModelAttribute("note") Note note, Model model) {
        if (note.getEtudiant() == null || note.getEtudiant().getId() == null
                || note.getCours() == null || note.getCours().getId() == null) {
            throw new IllegalStateException("Note missing Etudiant or Cours information");
        }

        noteService.createOrUpdateNote(note);

        return "redirect:/professeur/notes/" + note.getCours().getId();
    }





}