package com.gestionecole.service;

import com.gestionecole.repository.EtudiantRepository;
import com.gestionecole.repository.ProfesseurRepository;
import com.gestionecole.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilisateurDetailsServiceTest {

    @Mock(lenient = true)
    UtilisateurRepository utilisateurRepository;


    @Mock
    EtudiantRepository etudiantRepository;

    @Mock
    ProfesseurRepository professeurRepository;

    @InjectMocks
    UtilisateurDetailsService utilisateurDetailsService;

    @Test
    void throwsExceptionIfUserNotFound() {
        String email = "notfound@ecole.be";
        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            utilisateurDetailsService.loadUserByUsername(email);
        });
    }
}
