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
public class Professeur extends Utilisateur {

    @OneToMany(mappedBy = "professeur")
    private List<Cours> cours;

    @Override
    public String toString() {
        return "Professeur(nom=" + getNom() + ", email=" + getEmail() + ")";
    }
}
