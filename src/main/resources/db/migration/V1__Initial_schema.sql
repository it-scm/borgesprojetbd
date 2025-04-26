-- V1__Initial_schema.sql

CREATE TABLE eleve (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       nom VARCHAR(255) NOT NULL,
                       prenom VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE professeur (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            nom VARCHAR(255) NOT NULL,
                            prenom VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE cours (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       nom VARCHAR(255) NOT NULL,
                       description TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE inscription (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             eleve_id BIGINT NOT NULL,
                             cours_id BIGINT NOT NULL,
                             date_inscription DATE NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (eleve_id) REFERENCES eleve(id),
                             FOREIGN KEY (cours_id) REFERENCES cours(id)
);

CREATE TABLE enseigne (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          professeur_id BIGINT NOT NULL,
                          cours_id BIGINT NOT NULL,
                          date_debut DATE NOT NULL,
                          date_fin DATE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (professeur_id) REFERENCES professeur(id),
                          FOREIGN KEY (cours_id) REFERENCES cours(id)
);

CREATE TABLE examen (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        cours_id BIGINT NOT NULL,
                        date_examen DATE NOT NULL,
                        description TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (cours_id) REFERENCES cours(id)
);

CREATE TABLE composition (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             examen_id BIGINT NOT NULL,
                             eleve_id BIGINT NOT NULL,
                             note DECIMAL(5,2),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (examen_id) REFERENCES examen(id),
                             FOREIGN KEY (eleve_id) REFERENCES eleve(id)
);

CREATE TABLE resultat (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          eleve_id BIGINT NOT NULL,
                          examen_id BIGINT NOT NULL,
                          note_id BIGINT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (eleve_id) REFERENCES eleve(id),
                          FOREIGN KEY (examen_id) REFERENCES examen(id),
                          FOREIGN KEY (note_id) REFERENCES composition(id)
);
