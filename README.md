# 🎓 Gestion Scolaire - Projet de Gestion de Base de Données en Réseau

---

## 📚 Description

Gestion Scolaire est une application web de gestion d'école construite avec des technologies modernes.

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
- Docker & Docker Compose (si tu veux utiliser la stack en conteneurs)
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
- Flyway `clean` + `migrate` se lance automatiquement.
- La base est régénérée à partir des scripts SQL (`db/migration`).

---

### 4. Utilisateurs prédéfinis

Tous les utilisateurs utilisent le mot de passe **`Pass1234`**.

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

## 1. Installer Docker et Docker Compose sur Windows

Si tu n'as pas encore installé Docker :

- Télécharge Docker Desktop pour Windows : [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
- Installe-le en suivant l'assistant d'installation.
- Active l'intégration WSL 2 (Docker propose l'activation automatique).
- Redémarre ton PC si nécessaire.

Après installation :
```bash
docker --version
docker-compose --version
```
Ces deux commandes doivent fonctionner.

Docker Compose est inclus nativement dans Docker Desktop depuis les dernières versions.

---

## 2. Lancer PostgreSQL avec Docker Compose

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

Puis exécuter :

```bash
docker-compose up -d
```

✅ PostgreSQL sera disponible sur `localhost:5432` avec :
- **Database** : `ecole_db`
- **User** : `postgres`
- **Password** : `postgres`

---

## 3. Lancer l'application Spring Boot

Depuis IntelliJ, toujours avec :

```text
--spring.profiles.active=dev
```

La connexion se fera directement sur la base PostgreSQL Dockerisée.

---

## 🎯 Bonnes pratiques

- Toujours utiliser `--spring.profiles.active=dev` en local.
- Ne jamais désactiver `flyway.clean-disabled: true` en production !
- Utiliser Docker pour isoler ta base sans polluer ton OS.
- Contrôler les logs de démarrage pour corriger rapidement d'éventuelles erreurs Flyway ou Hibernate.

---

## 📜 Licence

Projet éducatif sous licence libre pour démonstration et apprentissage.
