package com.gestionecole.controller;

import com.gestionecole.service.SectionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/sections") // Example admin mapping
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public String listeSections(Model model) {
        model.addAttribute("sections", sectionService.getAllSections());
        return "admin/sections/liste"; // You might need an admin section in templates
    }

    // Add other methods for managing sections if needed
}