package com.gestionecole.config;

import com.gestionecole.repository.EtudiantRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final EtudiantRepository etudiantRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            log.debug("Authentication success for user: {}", email);

            // Vérifie si c'est un étudiant
            etudiantRepository.findByEmail(email).ifPresentOrElse(
                    etudiant -> {
                        try {
                            if (etudiant.getSection() == null) {
                                log.info("Etudiant sans section détecté : redirection vers la page d'inscription");
                                response.sendRedirect("/auth/register");
                            } else {
                                log.info("Etudiant inscrit : redirection vers ses cours");
                                response.sendRedirect("/etudiant/cours");
                            }
                        } catch (IOException e) {
                            log.error("Erreur lors de la redirection après authentification", e);
                        }
                    },
                    () -> {
                        try {
                            // Sinon c'est un professeur
                            log.info("Professeur détecté : redirection vers ses cours");
                            response.sendRedirect("/professeur/cours");
                        } catch (IOException e) {
                            log.error("Erreur lors de la redirection après authentification", e);
                        }
                    }
            );
        } else {
            log.error("Authentication principal n'est pas de type UserDetails : {}", principal.getClass().getName());
            response.sendRedirect("/auth/login?error=true");
        }
    }
}
