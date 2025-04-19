Cahier des charges du projet de gestion d'école :
Technologies utilisées :


Backend : Spring Boot (version 3.x)


Frontend : Thymeleaf, HTML, CSS (pas de JavaScript)


Base de données : MySQL


Version Java : 17


Outils : IntelliJ IDEA, Maven


Spring Security pour la gestion des rôles (professeur et étudiant)


Rôles utilisateurs :


Professeur : Peut voir ses cours, ses horaires, et les notes des étudiants dans ses cours. Il peut également modifier les notes des étudiants.


Étudiant : Peut se connecter, voir ses cours, ses horaires et ses notes. Il ne peut pas modifier ses informations.


Fonctionnalités spécifiques :


Page de connexion (Page d'accueil) :


Affiche un formulaire de connexion avec email et mot de passe.


Lien vers la page d'inscription pour les étudiants non inscrits.


Le rôle de l'utilisateur (étudiant ou professeur) détermine l'accès aux pages (étudiant ou professeur).


Lors de la connexion, si l'étudiant n'est pas inscrit, il doit être redirigé vers la page d'inscription.


Page d'inscription :


Permet aux étudiants de remplir un formulaire d'inscription avec leurs informations et de choisir leur section (Télécom, Cyber, Electronique).


Lors de l'inscription, l'étudiant voit le nombre de places restantes dans la section choisie.


Le mot de passe de l’étudiant doit être encodé avant d’être stocké dans la base de données.
une fois l’inscription fini l’etudiant doit appuyer sur retour à la page principale


Professeur :


Peut accéder aux pages de gestion de ses cours (affichage des notes, des horaires et des étudiants).


Peut modifier les notes des étudiants, mais ne peut pas modifier les cours ou les horaires.


La note de la deuxième session ne peut être ajoutée tant que la note de la première session n’a pas été donnée.


Peut voir ses horaires (professeur associé à ses horaires).


Étudiant :


Peut accéder à une page qui affiche ses cours et ses horaires.


Peut uniquement consulter ses notes et ne peut pas les modifier.


Il est impossible pour un étudiant de se réinscrire après une première inscription réussie.


Architecture du projet :


Structure des entités (modèles) :


Utilisateur : Contient l'email, mot de passe et rôle (étudiant ou professeur).


Professeur : Hérite de Utilisateur, avec des informations supplémentaires sur le professeur.


Etudiant : Hérite de Utilisateur, avec des informations supplémentaires sur l'étudiant.


Section : Contient les différentes sections disponibles (Télécom, Cyber, Electronique).


Cours : Les cours proposés dans l'école, chaque cours ayant un code, un nom, un nombre de crédits, et une description.


Horaire : Associe les cours à des horaires spécifiques.


Inscription : Lien entre l’étudiant et les cours auxquels il est inscrit.



Sécurité :


Gestion des rôles avec Spring Security :


Rôle ETUDIANT : Accès aux pages réservées aux étudiants (cours, horaires, notes).


Rôle PROFESSEUR : Accès aux pages réservées aux professeurs (cours, horaires, notes des étudiants).


Sécurisation de la page d'authentification, avec redirection en cas d'authentification incorrecte.


Le rôle (ETUDIANT, PROFESSEUR) est défini directement dans l’entité Utilisateur.


Base de données :


Tables principales :


Utilisateur : Contient les utilisateurs avec un champ pour leur rôle.


Professeur : Hérite de Utilisateur.


Etudiant : Hérite de Utilisateur.


Section : Contient les différentes sections disponibles.


Cours : Contient tous les cours avec leurs informations (code, description, crédits).


Horaire : Lien entre les cours et les horaires.


Inscription : Lien entre les étudiants et les cours auxquels ils sont inscrits.


Note : Contient les notes des étudiants pour chaque session.


Pages HTML (Front-end) :


Login Page : Formulaire de connexion pour l’authentification des utilisateurs.


Register Page : Formulaire d'inscription pour les étudiants non inscrits.


Etudiant Pages :


Voir ses cours, horaires et notes.


Aucune modification des informations autorisée.


Professeur Pages :


Voir ses cours, horaires et les notes des étudiants.


Possibilité de modifier les notes des étudiants.


Footer/Header : Présent sur toutes les pages pour une navigation fluide avec un lien vers la page principale.


Logique de gestion des notes :


Première session : Si la note de la première session n’a pas été enregistrée, la note de la deuxième session ne peut pas être ajoutée.


Gestion des sessions : Les notes sont gérées en fonction des sessions, une session étant liée à un étudiant et un cours spécifique.


Gestion de l'inscription :


Étudiant non inscrit : Lors de la première connexion, un étudiant peut s'inscrire s’il n’a pas déjà un compte.


Validation des places : L’étudiant voit le nombre de places disponibles pour chaque section (Télécom, Cyber, Electronique).
