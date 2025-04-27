package com.gestionecole.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            log.debug("Authentication success for user: {}", email);

            boolean isEtudiant = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ETUDIANT"));

            boolean isProfesseur = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROFESSEUR"));

            if (isEtudiant) {
                log.info("Redirection Etudiant vers /etudiant/cours");
                response.sendRedirect(request.getContextPath() + "/etudiant/cours");
            } else if (isProfesseur) {
                log.info("Redirection Professeur vers /professeur/cours");
                response.sendRedirect(request.getContextPath() + "/professeur/cours");
            } else {
                log.error("Utilisateur avec rôle inconnu : {}", authentication.getAuthorities());
                response.sendRedirect(request.getContextPath() + "/auth/login?error=true");
            }
        } else {
            log.error("Authentication principal n'est pas de type UserDetails : {}", principal.getClass().getName());
            response.sendRedirect(request.getContextPath() + "/auth/login?error=true");
        }
    }

}
