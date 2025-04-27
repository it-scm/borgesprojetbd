# 🎓 Gestion Scolaire - Projet de Gestion de Base de Données en Réseau

## 📚 Description

Gestion Scolaire est une application de gestion d'école

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
- Docker & Docker Compose (si tu veux utiliser la stack en conteneurs)
- IntelliJ IDEA (recommandé)

---

### 2. Installation sans Docker

#### a) Créer la base PostgreSQL localement

```bash
createdb ecole_db
```

**ou**

Utiliser `psql` :
```bash
psql -U postgres
CREATE DATABASE ecole_db;
```

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
2. Ajouter dans les **Program arguments** :

```text
--spring.profiles.active=dev
```

3. Lancer la classe `GestionScolaireApplication.java`.

⚡ **À chaque démarrage** :
- `flyway clean` et `flyway migrate` seront exécutés automatiquement.
- La base est régénérée à partir des scripts SQL `db/migration/`.

---

### 4. Utilisateurs prédéfinis

**Tous les utilisateurs ont le **même mot de passe** : `Pass1234`**

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

## 📋 Fonctionnalités

### Étudiants
- Consulter leurs cours.
- Consulter leurs horaires.
- Voir leurs notes (lecture seule).
- S'inscrire à une section (si non inscrit).

### Professeurs
- Voir la liste de leurs cours.
- Gérer les notes des étudiants.
- Voir leur horaire de cours.

---

# 🐳 Lancer avec Docker et Docker Compose

### 1. Docker Compose File (exemple `docker-compose.yml`)

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

### 2. Lancer la base

```bash
docker-compose up -d
```

✅ PostgreSQL sera disponible sur `localhost:5432` avec :
- **Database** : `ecole_db`
- **User** : `postgres`
- **Password** : `postgres`

### 3. Lancer l'application Spring Boot
Comme précédemment dans IntelliJ avec profil `dev`.

---

## 🎯 Bonnes pratiques

- Toujours utiliser `--spring.profiles.active=dev` en local.
- Ne jamais désactiver `flyway.clean-disabled` en production !
- Utiliser la base Dockerisée pour éviter d'impacter ton PostgreSQL local.
- Contrôler les logs au démarrage (`INFO`, `DEBUG`) pour détecter les erreurs Flyway, Hibernate, Spring Security.

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

## 📜 Licence

Projet éducatif sous licence libre pour démonstration et apprentissage.

