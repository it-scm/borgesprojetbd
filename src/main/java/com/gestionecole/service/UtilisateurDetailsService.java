package com.gestionecole.service;

import com.gestionecole.model.Utilisateur;
import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.ProfesseurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UtilisateurDetailsService implements UserDetailsService {

    private final EtudiantRepository etudiantRepository;
    private final ProfesseurRepository professeurRepository;

    public UtilisateurDetailsService(EtudiantRepository etudiantRepository,
                                     ProfesseurRepository professeurRepository) {
        this.etudiantRepository = etudiantRepository;
        this.professeurRepository = professeurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("loadUserByUsername Trying to load user by email: {}", email);

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

