package com.gestionecole.config;

import com.gestionecole.service.UtilisateurDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UtilisateurDetailsService utilisateurDetailsService;

    public SecurityConfig(UtilisateurDetailsService utilisateurDetailsService) {
        this.utilisateurDetailsService = utilisateurDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        MvcRequestMatcher h2RequestMatcher = new MvcRequestMatcher(introspector, "/h2-console/**");

        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(h2RequestMatcher))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/static/**").permitAll()
                        .requestMatchers("/etudiant/**").hasRole("ETUDIANT")
                        .requestMatchers("/professeur/**").hasRole("PROFESSEUR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                )
                .userDetailsService(utilisateurDetailsService);

        return http.build();
    }
}