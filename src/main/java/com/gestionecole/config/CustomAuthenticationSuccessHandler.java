package com.gestionecole.config;

import com.gestionecole.model.Etudiant;
import com.gestionecole.service.EtudiantService;
import com.gestionecole.service.ProfesseurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ApplicationContext context;

    public CustomAuthenticationSuccessHandler(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();

        // Lazy load to break circular dependency
        ProfesseurService professeurService = context.getBean(ProfesseurService.class);
        EtudiantService etudiantService = context.getBean(EtudiantService.class);

        if (professeurService.getProfesseurByEmail(email).isPresent()) {
            response.sendRedirect("/professeur/cours");
            return;
        }

        if (etudiantService.getEtudiantByEmail(email).isPresent()) {
            Etudiant etudiant = etudiantService.getEtudiantWithSectionByEmail(email).orElseThrow();
            if (!etudiant.isInscrit()) {
                response.sendRedirect("/register");
            } else {
                response.sendRedirect("/etudiant/cours");
            }
            return;
        }

        response.sendRedirect("/home");
    }
}
