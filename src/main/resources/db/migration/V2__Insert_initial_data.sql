-- ================================
-- V2__Insert_initial_data.sql
-- Initial sample data for Gestion Ecole
-- ================================

-- Insert sections
INSERT INTO section (nom, nb_places) VALUES
                                         ('Télécom', 30),
                                         ('Cyber', 25),
                                         ('Électronique', 20);

-- Insert academic years
INSERT INTO annee_section (annee_academique, section_id) VALUES
                                                             ('2024-2025', 1),
                                                             ('2024-2025', 2),
                                                             ('2024-2025', 3);
--$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa

-- Insert Students
INSERT INTO utilisateur (email, nom, prenom, password, role, matricule) VALUES
                                                                            ('alice.dupont@ecole.be', 'Dupont', 'Alice', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_ETUDIANT', 'E-10001'),
                                                                            ('bob.martin@ecole.be', 'Martin', 'Bob', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_ETUDIANT', 'E-10002'),
                                                                            ('charlie.petit@ecole.be', 'Petit', 'Charlie', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_ETUDIANT', 'E-10003');

-- Insert Professors
INSERT INTO utilisateur (email, nom, prenom, password, role, matricule) VALUES
                                                                            ('michel.bernair@ecole.be', 'Bernair', 'Michel', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20001'),
                                                                            ('jean-paul.hecquet@ecole.be', 'Hecquet', 'Jean-Paul', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20002'),
                                                                            ('ali.jaghou@ecole.be', 'Jaghou', 'Ali', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20003'),
                                                                            ('david.lemaire@ecole.be', 'Lemaire', 'David', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20004');

-- Insert etudiants (after utilisateur insertions)
INSERT INTO etudiant (id, section_id, annee_section_id, info, photo) VALUES
                                                                         (1, 1, 1, NULL, NULL),
                                                                         (2, NULL, NULL, NULL, NULL),
                                                                         (3, NULL, NULL, NULL, NULL);

-- Insert professeurs (after utilisateur insertions)
INSERT INTO professeur (id, matricule) VALUES
                                           (4, 'R-10001'),
                                           (5, 'R-10002'),
                                           (6, 'R-10003'),
                                           (7, 'R-10004');

-- Insert test courses linked to a professor with id = 4
INSERT INTO cours (code, intitule, description, credits, professeur_id) VALUES
                                                                            ('UE299', 'Bases des réseaux', 'Introduction aux réseaux TCP/IP', 5, 4),
                                                                            ('UE301', 'Bases de données', 'Conception et gestion de bases de données', 4, 4),
                                                                            ('UE302', 'Analyse informatique', 'Introduction à l''analyse de systèmes informatiques', 4, 4);


-- Insert schedules (horaires)
INSERT INTO horaire (annee_section_id, cours_id, heure_debut, heure_fin, jour)
VALUES (1, 2, '08:00:00', '10:00:00', 'Lundi');



-- Insert into inscription (student enrollments)
INSERT INTO inscription (etudiant_id, cours_id) VALUES
                                                    (1, 1), -- Alice enrolled in UE299
                                                    (2, 1), -- Bob enrolled in UE299
                                                    (3, 1), -- Charlie enrolled in UE299
                                                    (1, 2), -- Alice enrolled in UE301
                                                    (2, 3); -- Bob enrolled in UE302

-- Insert empty notes (only needed if you prefer eager creation, optional)
-- Otherwise, the system will create them when needed.
-- Insert into note (student grades)
INSERT INTO note (etudiant_id, cours_id, premiere_session, deuxieme_session) VALUES
                                                                                 (1, 1, NULL, NULL),
                                                                                 (2, 1, NULL, NULL),
                                                                                 (3, 1, NULL, NULL),
                                                                                 (1, 2, NULL, NULL),
                                                                                 (2, 3, NULL, NULL);
