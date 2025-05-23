package com.gestionecole.controller;

import com.gestionecole.model.Etudiant;
import com.gestionecole.service.EtudiantService;
import com.gestionecole.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // Public registration for new students
    @GetMapping("/auth/register")
    public String registerForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());  // 🔥 empty form for new Etudiant
        model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String register(
            @Valid @ModelAttribute("etudiant") Etudiant formEtudiant,
            BindingResult result,
            @RequestParam("sectionId") Long sectionId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
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
            String currentAcademicYear = etudiantService.getCurrentAcademicYear();
            etudiantService.registerStudent(formEtudiant, sectionId, currentAcademicYear);
        } catch (IllegalStateException e) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces());
            model.addAttribute("sectionError", e.getMessage());
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie ! Veuillez vous connecter.");
        return "redirect:/auth/login";
    }
}
