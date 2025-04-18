# Projet Gestion École

Ce projet est une application web de gestion d'école développée avec Spring Boot, Thymeleaf, MySQL, Lombok.

## Fonctionnalités

- ✅ Connexion sécurisée (Spring Security)
- ✅ Inscription des étudiants avec sélection de section (et places restantes affichées sans JS)
- ✅ Redirection automatique après login (professeurs ou étudiants)
- ✅ Professeurs peuvent voir leurs cours, voir les étudiants inscrits et modifier les notes
- ✅ Étudiants peuvent voir leurs cours, horaires et notes
- ✅ Base de données initialisée avec :
    - 4 professeurs
    - 3 sections (Télécom, Cyber, Électronique)
    - 3 cours (UE299, UE301, UE302)
    - Horaires pour chaque section et année

## Identifiants de test

| Email                          | Mot de passe | Rôle       |
|-------------------------------|--------------|------------|
| michel.bernair@ecole.be       | Pass1234     | PROFESSEUR |
| jean-paul.hecquet@ecole.be    | Pass1234     | PROFESSEUR |
| ali.jaghou@ecole.be           | Pass1234     | PROFESSEUR |
| david.lemaire@ecole.be        | Pass1234     | PROFESSEUR |

Les étudiants doivent s'inscrire eux-mêmes via le formulaire d'inscription.

## Lancement du projet

1. Cloner le projet dans IntelliJ
2. Créer une base de données nommée `ecole`
3. Modifier `application.properties` si nécessaire (pour vos identifiants MySQL)
4. Lancer `GestionScolaireApplication.java`

---

© 2024 - Projet éducatif développé avec Spring Boot, Thymeleaf et MySQL.
