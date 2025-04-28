# 🎓 Gestion Scolaire - Projet de Gestion de Base de Données en Réseau

---

## 📜 Cahier des charges

### Technologies utilisées

- **Backend** : Spring Boot 3.x
- **Frontend** : Thymeleaf, HTML, CSS (pas de JavaScript)
- **Base de données** : PostgreSQL (remplacement de MySQL)
- **Version Java** : 17+
- **Outils** : IntelliJ IDEA, Maven
- **Sécurité** : Spring Security pour la gestion des rôles (Professeur et Étudiant)

### Rôles utilisateurs

#### Professeur
- Peut voir ses cours, ses horaires, et les notes des étudiants dans ses cours.
- Peut modifier les notes des étudiants (pas les cours ni les horaires).
- Ne peut ajouter la note de deuxième session qu'après la première session.
- Peut consulter son horaire.

#### Étudiant
- Peut se connecter, voir ses cours, horaires et notes.
- Ne peut pas modifier ses informations.
- Impossibilité de se réinscrire après une inscription réussie.

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
- `flyway clean` + `migrate` sont exécutés automatiquement.
- La base est régénérée à partir des scripts SQL (`db/migration`).

---

### 4. Utilisateurs prédéfinis

Tous les utilisateurs utilisent le même mot de passe : **`Pass1234`**.

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

- [http://localhost:4242/auth/login](http://localhost:4242/auth/login)

---

## 📋 Fonctionnalités principales

### Étudiants
- Voir ses cours
- Voir son horaire
- Voir ses notes (lecture seule)

### Professeurs
- Voir ses cours
- Voir son horaire
- Voir et modifier les notes des étudiants

---

# 🐳 Lancer avec Docker et Docker Compose

## 1. Installer Docker et Docker Compose sur Windows

- Télécharger Docker Desktop : [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
- Suivre l'installation.
- Activer WSL 2 si nécessaire.
- Vérifier avec :

```bash
docker --version
docker-compose --version
```

✅ Docker Compose est intégré à Docker Desktop.

---

## 2. Docker Compose pour PostgreSQL

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

Lancer la base :

```bash
docker-compose up -d
```

✅ PostgreSQL sera disponible sur `localhost:5432`.

---

## 3. Lancer Spring Boot

Lancer dans IntelliJ avec :

```text
--spring.profiles.active=dev
```

---

## 🗄️ Contenu initial de la base de données

Lors de chaque démarrage, la base est remplie automatiquement :

### Sections
- Télécom (30 places)
- Cyber (25 places)
- Électronique (20 places)

### Utilisateurs
#### Étudiants
| ID | Email                    | Section  | Année       |
|----|---------------------------|----------|-------------|
| 1  | alice.dupont@ecole.be      | Télécom  | 2024-2025   |
| 2  | bob.martin@ecole.be        | Télécom  | 2024-2025   |
| 3  | charlie.petit@ecole.be     | Télécom  | 2024-2025   |

#### Professeurs
| ID | Email                      | Matricule  |
|----|-----------------------------|------------|
| 4  | michel.bernair@ecole.be      | P-20001    |
| 5  | jean-paul.hecquet@ecole.be   | P-20002    |
| 6  | ali.jaghou@ecole.be          | P-20003    |
| 7  | david.lemaire@ecole.be       | P-20004    |

### Cours
| Code    | Intitulé                  | Professeur |
|---------|----------------------------|------------|
| NET101  | Fundamentals of Networking  | Bernair    |
| NET201  | Routing and Switching       | Bernair    |
| NET301  | Network Security            | Bernair    |
| NET401  | Wireless Networking         | Bernair    |
| NET501  | Cloud Networking            | Bernair    |

### Horaires
| Jour       | Heure début | Heure fin | Cours |
|------------|-------------|-----------|-------|
| Lundi      | 18:00        | 21:30     | NET101 |
| Mardi      | 18:00        | 21:30     | NET201 |
| Mercredi   | 18:00        | 21:30     | NET301 |
| Jeudi      | 18:00        | 21:30     | NET401 |
| Vendredi   | 18:00        | 21:30     | NET501 |

### Inscriptions
- Alice Dupont : NET101, NET201, NET301
- Bob Martin : NET101, NET201, NET301, NET401, NET501
- Charlie Petit : NET401, NET501

### Notes
- Initialisées à `NULL` pour toutes les matières et étudiants.

---

## 🎯 Bonnes pratiques

- Toujours utiliser `--spring.profiles.active=dev` en local.
- Ne jamais désactiver `flyway.clean-disabled` en production !
- Contrôler les logs au démarrage pour identifier rapidement les erreurs.

---

## 📜 Licence

Projet éducatif à but pédagogique sous licence libre.

