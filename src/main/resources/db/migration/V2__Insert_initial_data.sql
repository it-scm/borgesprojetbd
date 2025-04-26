-- Insert into section
INSERT INTO section (nom, nb_places) VALUES
                                         ('Télécom', 30),
                                         ('Cyber', 25),
                                         ('Électronique', 20);

-- Insert into annee_section
INSERT INTO annee_section (annee_academique, section_id) VALUES
                                                             ('2024-2025', 1),
                                                             ('2024-2025', 2),
                                                             ('2024-2025', 3);

-- Insert utilisateurs (students and professors)

-- Students
INSERT INTO utilisateur (email, nom, prenom, password, role) VALUES
                                                                 ('alice.dupont@ecole.be', 'Dupont', 'Alice', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_ETUDIANT'),
                                                                 ('bob.martin@ecole.be', 'Martin', 'Bob', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_ETUDIANT'),
                                                                 ('charlie.petit@ecole.be', 'Petit', 'Charlie', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_ETUDIANT');

-- Professors
INSERT INTO utilisateur (email, nom, prenom, password, role) VALUES
                                                                 ('michel.bernair@ecole.be', 'Bernair', 'Michel', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_PROFESSEUR'),
                                                                 ('jean-paul.hecquet@ecole.be', 'Hecquet', 'Jean-Paul', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_PROFESSEUR'),
                                                                 ('ali.jaghou@ecole.be', 'Jaghou', 'Ali', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_PROFESSEUR'),
                                                                 ('david.lemaire@ecole.be', 'Lemaire', 'David', '$2a$12$F7N.F46fPAhKrOS/.E5bLuDXUSHvyh3VcoLJ1EnH0bLBhzxjydDlO', 'ROLE_PROFESSEUR');

-- Insert etudiant
INSERT INTO etudiant (id, section_id, annee_section_id, info, photo) VALUES
                                                                         (1, 1, 1, NULL, NULL),
                                                                         (2, NULL, NULL, NULL, NULL),
                                                                         (3, NULL, NULL, NULL, NULL);

-- Insert professeur
INSERT INTO professeur (id, matricule) VALUES
                                           (4, 'R-10001'),
                                           (5, 'R-10002'),
                                           (6, 'R-10003'),
                                           (7, 'R-10004');

-- Insert cours
INSERT INTO cours (code, intitule, credits, professeur_id) VALUES
                                                               ('UE299', 'Bases des réseaux', 5, 4),
                                                               ('UE301', 'Bases de données', 4, 4),
                                                               ('UE302', 'Analyse informatique', 4, 4);

-- Insert horaire
INSERT INTO horaire (jour, heure_debut, heure_fin, cours_id, annee_section_id) VALUES
    ('Lundi', '08h00', '10h00', 1, 1);
