package com.gestionecole.model;

import jakarta.persistence.*;

@Entity
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String intitule;
    private int credits;
    private String description;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @ManyToOne
    @JoinColumn(name = "annee_section_id")
    private AnneeSection anneeSection;

    // ✅ SUPPRIMÉ : relation ManyToMany non nécessaire
    // private List<Professeur> professeurs;

    public Cours() {
    }

    public Cours(String code, String intitule, int credits, String description, Professeur professeur, AnneeSection anneeSection) {
        this.code = code;
        this.intitule = intitule;
        this.credits = credits;
        this.description = description;
        this.professeur = professeur;
        this.anneeSection = anneeSection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public AnneeSection getAnneeSection() {
        return anneeSection;
    }

    public void setAnneeSection(AnneeSection anneeSection) {
        this.anneeSection = anneeSection;
    }
}
