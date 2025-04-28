package com.gestionecole.service;

import com.gestionecole.model.Utilisateur;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.ProfesseurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
public class UtilisateurDetailsService implements UserDetailsService {

    private final EtudiantRepository etudiantRepository;
    private final ProfesseurRepository professeurRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");


    public UtilisateurDetailsService(EtudiantRepository etudiantRepository,
                                     ProfesseurRepository professeurRepository) {
        this.etudiantRepository = etudiantRepository;
        this.professeurRepository = professeurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("loadUserByUsername - retrouve l'utilisateur par email: {}", email);

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            log.warn("Tentative de connexion avec un email invalide : {}", email);
            throw new BadCredentialsException("Format de l'adresse email invalide");
        }

        Utilisateur utilisateur = etudiantRepository.findByEmail(email)
                .map(u -> (Utilisateur) u)
                .or(() -> professeurRepository.findByEmail(email).map(u -> (Utilisateur) u))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return User.withUsername(utilisateur.getEmail())
                .password(utilisateur.getPassword())
                .roles(utilisateur.getRole().replaceFirst("^ROLE_", ""))
                .build();
    }
}

