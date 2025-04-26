package com.gestionecole.controller;

import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Section;
import com.gestionecole.service.EtudiantService;
import com.gestionecole.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.userdetails.User;

@Controller
public class AuthController {

    private final EtudiantService etudiantService;
    private final SectionService sectionService;

    public AuthController(EtudiantService etudiantService, SectionService sectionService) {
        this.etudiantService = etudiantService;
        this.sectionService = sectionService;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String registerForm(Model model, @AuthenticationPrincipal User user) {
        // Find the Etudiant by email from the database
        Etudiant etudiant = etudiantService.getEtudiantByEmail(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Étudiant non trouvé"));

        model.addAttribute("etudiant", etudiant);
        model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces());
        return "auth/register";
    }


    @PostMapping("/auth/register")
    public String register(
            @Valid @ModelAttribute("etudiant") Etudiant formEtudiant,
            BindingResult result,
            Model model,
            @RequestParam("sectionId") Long sectionId,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        Section section = sectionService.getSectionById(sectionId)
                .orElseThrow(() -> new IllegalStateException("Section non trouvée"));

        int placesRestantes = section.getNbPlaces() - etudiantService.countEtudiantsInSection(sectionId);

        if (result.hasErrors()) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces());
            return "auth/register";
        }

        if (!formEtudiant.getEmail().matches("^[a-zA-Z]+\\.[a-zA-Z]+@ecole\\.be$")) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces());
            model.addAttribute("emailError", "Le format de l'email doit être prenom.nom@ecole.be");
            return "auth/register";
        }

        try {
            // 🔥 Fetch existing Etudiant by email (connected user)
            Etudiant existingEtudiant = etudiantService.getEtudiantByEmail(user.getUsername())
                    .orElseThrow(() -> new IllegalStateException("Étudiant non trouvé"));

            // 🔥 Update the section
            existingEtudiant.setSection(section);

            // 🔥 Save updated Etudiant
            etudiantService.save(existingEtudiant);

            redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie ! Veuillez vous connecter.");
            return "redirect:/auth/login";
        } catch (IllegalStateException e) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces());
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
