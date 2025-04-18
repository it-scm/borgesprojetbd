package com.gestionecole.service;

import com.gestionecole.model.Professeur;
import com.gestionecole.repository.ProfesseurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesseurService implements UserDetailsService { // Implements UserDetailsService

    private final ProfesseurRepository professeurRepository;

    public ProfesseurService(ProfesseurRepository professeurRepository) {
        this.professeurRepository = professeurRepository;
    }

    public List<Professeur> getAllProfesseurs() {
        return professeurRepository.findAll();
    }

    public Optional<Professeur> getProfesseurById(Long id) {
        return professeurRepository.findById(id);
    }

    public Optional<Professeur> getProfesseurByEmail(String email) {
        return professeurRepository.findByEmail(email);
    }

    public Professeur saveProfesseur(Professeur professeur) {
        return professeurRepository.save(professeur);
    }

    public void deleteProfesseur(Long id) {
        professeurRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Professeur> professeur = professeurRepository.findByEmail(email);
        if (professeur.isEmpty()) {
            throw new UsernameNotFoundException("Professeur non trouvé avec l'email : " + email);
        }
        Professeur user = professeur.get();
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("PROFESSEUR")
                .build();
    }
}