package com.gestionecole.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends Utilisateur {

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "etudiant")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "etudiant")
    private List<Note> notes;

    @Transient
    private Note noteForCours;

    @ManyToOne
    @JoinColumn(name = "annee_section_id")
    private AnneeSection anneeSection;

    private String info;
    private String photo;

    public boolean isInscrit() {
        return this.section != null;
    }

    @Override
    public String toString() {
        return "Etudiant(nom=" + getNom() +
                ", prenom=" + getPrenom() +
                ", email=" + getEmail() + ")";
    }
}