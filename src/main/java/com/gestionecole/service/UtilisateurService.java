package com.gestionecole.service;

import com.gestionecole.model.Utilisateur;
import com.gestionecole.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    public Utilisateur getById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

}