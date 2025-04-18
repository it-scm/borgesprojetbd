package com.gestionecole.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id") // ✅ indique que Professeur réutilise la clé primaire de Utilisateur
@Getter
@Setter
@NoArgsConstructor
public class Professeur extends Utilisateur {

    @OneToMany(mappedBy = "professeur")
    private List<Cours> cours;

    private String matricule;
}
