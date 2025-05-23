package com.gestionecole.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int nbPlaces;

    @Transient
    private int placesRestantes; // Champ non stocké en base, mais utilisé pour l'affichage

    // Removed Etudiant list as Etudiant is no longer directly linked to Section.
    // Students of a section are now found via AnneeSection -> Inscription.
    // @OneToMany(mappedBy = "section")
    // private List<Etudiant> etudiants;

    @OneToMany(mappedBy = "section")
    private List<AnneeSection> anneeSections;

    public Section(String nom, int nbPlaces) {
        this.nom = nom;
        this.nbPlaces = nbPlaces;
    }

    @Override
    public String toString() {
        return "Section(nom=" + nom + ", nbPlaces=" + nbPlaces + ")";
    }
}
