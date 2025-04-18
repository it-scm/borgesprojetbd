package com.gestionecole.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor // Si vous utilisez Lombok, cette annotation génère le constructeur par défaut
public class Horaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jour;
    private String heureDebut;
    private String heureFin;

    @ManyToOne
    @JoinColumn(name = "cours_id")
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "annee_section_id")
    private AnneeSection anneeSection;

    // Constructors, Getters, Setters
    // public Horaire() { // Supprimez ce constructeur si vous utilisez @NoArgsConstructor
    // }

    public Horaire(String jour, String heureDebut, String heureFin, Cours cours, AnneeSection anneeSection) {
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.cours = cours;
        this.anneeSection = anneeSection;
    }

    // ... (autres méthodes)
}