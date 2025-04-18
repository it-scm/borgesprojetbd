package com.gestionecole.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor // Si vous utilisez Lombok, cette annotation génère le constructeur par défaut
public class AnneeSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String anneeAcademique;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    // Constructors, Getters, Setters
    // public AnneeSection() { // Supprimez ce constructeur si vous utilisez @NoArgsConstructor
    // }

    public AnneeSection(String anneeAcademique, Section section) {
        this.anneeAcademique = anneeAcademique;
        this.section = section;
    }

    // ... (autres méthodes)
}