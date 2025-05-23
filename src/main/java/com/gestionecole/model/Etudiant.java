package com.gestionecole.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends Utilisateur {

    @OneToMany(mappedBy = "etudiant")
    private List<Inscription> inscriptions;

    // Removed direct collection of Notes from Etudiant.
    // Notes should be accessed via etudiant.getInscriptions().get(i).getNotes()
    // or through NoteService.getNotesByEtudiant(etudiant)
    // @OneToMany(mappedBy = "inscription.etudiant")
    // private List<Note> notes;

    @Transient
    private Note noteForCours;

    private String info;
    private String photo;

    public boolean isInscrit() {
        // TODO: This logic needs to be updated as section is removed
        return false; 
    }

    @Override
    public String toString() {
        return "Etudiant(nom=" + getNom() +
                ", prenom=" + getPrenom() +
                ", email=" + getEmail() + ")";
    }
}