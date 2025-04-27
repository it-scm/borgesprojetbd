# 🎓 Gestion Scolaire - Projet de Gestion de Base de Données en Réseau

---
## Technologies utilisées

- **Backend** : Spring Boot 3.x
- **Frontend** : Thymeleaf, HTML, CSS
- **Base de données** : PostgreSQL
- **Version Java** : 17+
- **Outils** : IntelliJ IDEA, Maven
- **Sécurité** : Spring Security pour la gestion des rôles (Professeur et Étudiant)

---

## 📜 Cahier des charges

### Rôles utilisateurs

#### Professeur
- Peut voir ses cours, ses horaires, et les notes des étudiants dans ses cours.
- Peut modifier les notes des étudiants (mais pas les cours ni les horaires).
- Ne peut pas insérer une note de 2e session tant que celle de la 1re session n'est pas présente.
- Peut consulter son propre horaire.

#### Étudiant
- Peut se connecter, voir ses cours, horaires et notes.
- Ne peut modifier aucune information personnelle.
- Impossible de se réinscrire après une première inscription réussie.

### Fonctionnalités spécifiques

- Page de connexion avec email et mot de passe.
- Redirection automatique des étudiants non inscrits vers la page d'inscription.
- Page d'inscription pour choisir une section (Télécom, Cyber, Électronique) avec places disponibles.
- Mot de passe encodé avant stockage.
- Footer/Header présent sur toutes les pages pour la navigation.

### Architecture du projet

- **Entités** :
  - Utilisateur (hérité par Professeur et Étudiant)
  - Section
  - Cours
  - Horaire
  - Inscription
  - Note

- **Sécurité** :
  - Gestion stricte des accès avec rôles Spring Security (`ROLE_ETUDIANT`, `ROLE_PROFESSEUR`).
  - Redirection en cas d’échec de connexion.

- **Base de données** :
  - Tables principales : utilisateur, professeur, étudiant, section, cours, horaire, inscription, note.

---

## 🛠️ Technologies utilisées

- **Spring Boot 3.2.x**
- **Spring Security**
- **Spring Data JPA**
- **Thymeleaf**
- **Flyway 10**
- **PostgreSQL 15**
- **Docker & Docker Compose**
- **Maven 3.9+**

---

## 🚀 Démarrage rapide

### 1. Prérequis

- Java 21
- Maven 3.9+
- PostgreSQL 15+ ou Docker
- Docker & Docker Compose
- IntelliJ IDEA (recommandé)

---

### 2. Installation sans Docker

#### a) Créer la base PostgreSQL localement

```bash
createdb ecole_db
```

**ou via psql** :
```bash
psql -U postgres
CREATE DATABASE ecole_db;
```

---

#### b) Vérifier la configuration `src/main/resources/application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecole_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    clean-disabled: false
```

---

### 3. Lancer l'application dans IntelliJ

1. Aller dans `Run -> Edit Configurations`.
2. Ajouter dans **Program arguments** :

```text
--spring.profiles.active=dev
```

3. Lancer la classe `GestionScolaireApplication.java`.

⚡ À chaque démarrage :
- Flyway `clean` + `migrate` est exécuté automatiquement.
- La base est régénérée à partir des scripts SQL (`db/migration`).

---

### 4. Utilisateurs prédéfinis

Tous les utilisateurs utilisent le même mot de passe **`Pass1234`**.

#### Étudiants
```
alice.dupont@ecole.be
bob.martin@ecole.be
charlie.petit@ecole.be
```

#### Professeurs
```
michel.bernair@ecole.be
jean-paul.hecquet@ecole.be
ali.jaghou@ecole.be
david.lemaire@ecole.be
```

---

## 🌐 Accès à l'application

- Page de connexion : [http://localhost:4242/auth/login](http://localhost:4242/auth/login)

---

## 📋 Fonctionnalités principales

### Étudiants
- Consulter leurs cours.
- Consulter leurs horaires.
- Voir leurs notes.

### Professeurs
- Voir la liste de leurs cours.
- Gérer les notes des étudiants.
- Voir leur horaire de cours.

---

# 🐳 Lancer avec Docker et Docker Compose

## 1. Installer Docker et Docker Compose sur Windows

- Télécharger Docker Desktop : [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
- Installer Docker en suivant les instructions.
- Activer WSL 2 pendant l'installation si proposé.
- Vérifier que les commandes fonctionnent :

```bash
docker --version
docker-compose --version
```

✅ Docker Compose est inclus dans Docker Desktop.

---

## 2. Utiliser Docker Compose pour PostgreSQL

Créer un fichier `docker-compose.yml` :

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    restart: always
    container_name: postgres-ecole-db
    environment:
      POSTGRES_DB: ecole_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

Démarrer PostgreSQL :

```bash
docker-compose up -d
```

✅ PostgreSQL sera disponible sur `localhost:5432`.

---

## 3. Lancer Spring Boot

Dans IntelliJ, lancer la classe `GestionScolaireApplication.java` avec :

```text
--spring.profiles.active=dev
```

La base PostgreSQL Dockerisée sera utilisée automatiquement.

---

## 🎯 Bonnes pratiques

- Toujours utiliser `--spring.profiles.active=dev` en local.
- Ne jamais désactiver `flyway.clean-disabled` en production !
- Docker est recommandé pour isoler PostgreSQL du système local.
- Surveiller les logs au démarrage (`INFO`, `DEBUG`) pour repérer d'éventuelles erreurs.

---

## 📜 Licence

Projet éducatif à but pédagogique sous licence libre.
