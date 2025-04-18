package com.gestionecole.service;

import com.gestionecole.model.Utilisateur;
import com.gestionecole.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        User.UserBuilder userBuilder = User.withUsername(utilisateur.getEmail());
        userBuilder.password(utilisateur.getPassword());

        // ✅ Supprimer le préfixe "ROLE_" si déjà présent
        String role = utilisateur.getRole();
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        userBuilder.roles(role);

        return userBuilder.build();
    }
}
