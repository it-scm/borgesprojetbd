package com.gestionecole.controller;

import com.gestionecole.model.Etudiant;
import com.gestionecole.model.Section;
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

    @GetMapping("/auth/register")
    public String registerForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces()); // ✅ corrigé
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String register(
            @Valid @ModelAttribute("etudiant") Etudiant etudiant,
            BindingResult result,
            Model model,
            @RequestParam("sectionId") Long sectionId,
            RedirectAttributes redirectAttributes
    ) {
        Section section = sectionService.getSectionById(sectionId)
                .orElseThrow(() -> new IllegalStateException("Section non trouvée"));

        int placesRestantes = section.getNbPlaces() - etudiantService.countEtudiantsInSection(sectionId);

        if (result.hasErrors()) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces()); // ✅ corrigé
            return "auth/register";
        }

        if (!etudiant.getEmail().matches("^[a-zA-Z]+\\.[a-zA-Z]+@ecole\\.be$")) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces()); // ✅ corrigé
            model.addAttribute("emailError", "Le format de l'email doit être prenom.nom@ecole.be");
            return "auth/register";
        }

        try {
            etudiantService.registerStudent(etudiant, sectionId);
            redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie ! Veuillez vous connecter.");
            return "redirect:/auth/login";
        } catch (IllegalStateException e) {
            model.addAttribute("sections", sectionService.findAllSectionsWithRemainingPlaces()); // ✅ corrigé
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
