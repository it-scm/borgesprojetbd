-- ================================
-- V2__Insert_initial_data.sql
-- Insert initial data for Gestion Ecole
-- ================================

-- Insert Sections
INSERT INTO section (nom, nb_places) VALUES
                                         ('Télécom', 30),
                                         ('Cyber', 25),
                                         ('Électronique', 20);

-- Insert Academic Years
INSERT INTO annee_section (annee_academique, section_id) VALUES
                                                             ('2024-2025', 1),
                                                             ('2024-2025', 2),
                                                             ('2024-2025', 3);

-- Insert Users (utilisateur) - Etudiants
INSERT INTO utilisateur (id, email, nom, prenom, password, role, matricule) VALUES
                                                                                (1, 'alice.dupont@ecole.be', 'Dupont', 'Alice', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_ETUDIANT', 'E-10001'),
                                                                                (2, 'bob.martin@ecole.be', 'Martin', 'Bob', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_ETUDIANT', 'E-10002'),
                                                                                (3, 'charlie.petit@ecole.be', 'Petit', 'Charlie', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_ETUDIANT', 'E-10003');

-- Insert Users (utilisateur) - Professeurs
INSERT INTO utilisateur (id, email, nom, prenom, password, role, matricule) VALUES
                                                                                (4, 'michel.bernair@ecole.be', 'Bernair', 'Michel', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20001'),
                                                                                (5, 'jean-paul.hecquet@ecole.be', 'Hecquet', 'Jean-Paul', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20002'),
                                                                                (6, 'ali.jaghou@ecole.be', 'Jaghou', 'Ali', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20003'),
                                                                                (7, 'david.lemaire@ecole.be', 'Lemaire', 'David', '$2a$12$0aPcHqsZIIRMm0ZkuYe96e2gMLmW1V6zTqFUqKNLUiypSlAWxgMQa', 'ROLE_PROFESSEUR', 'P-20004');

-- Insert Students (etudiant)
INSERT INTO etudiant (id, section_id, annee_section_id, info, photo) VALUES
                                                                         (1, 1, 1, NULL, NULL),
                                                                         (2, 1, 1, NULL, NULL),
                                                                         (3, 1, 1, NULL, NULL);

-- Insert Professors (professeur)
INSERT INTO professeur (id, matricule) VALUES
                                           (4, 'P-20001'),
                                           (5, 'P-20002'),
                                           (6, 'P-20003'),
                                           (7, 'P-20004');

-- Insert Networking Courses linked to Professeur Michel Bernair (id=4)
INSERT INTO cours (code, intitule, credits, description, professeur_id) VALUES
                                                                            ('NET101', 'Fundamentals of Networking', 5, 'Introduction to computer networks, OSI model, TCP/IP basics.', 4),
                                                                            ('NET201', 'Routing and Switching', 5, 'Deep dive into routing protocols, VLANs, switching technologies.', 4),
                                                                            ('NET301', 'Network Security', 5, 'Principles of securing networks, firewalls, VPNs, and intrusion detection.', 4),
                                                                            ('NET401', 'Wireless Networking', 5, '802.11 standards, Wi-Fi configuration, security and troubleshooting.', 4),
                                                                            ('NET501', 'Cloud Networking', 5, 'Networking in cloud environments: AWS, Azure, hybrid networking.', 4);

-- Insert Horaire (Weekly Schedule)
INSERT INTO horaire (annee_section_id, cours_id, heure_debut, heure_fin, jour)
VALUES
    (1, 1, '18:00', '21:30', 'LUNDI'),
    (1, 2, '18:00', '21:30', 'MARDI'),
    (1, 3, '18:00', '21:30', 'MERCREDI'),
    (1, 4, '18:00', '21:30', 'JEUDI'),
    (1, 5, '18:00', '21:30', 'VENDREDI');

-- Insert Inscriptions (Enrollment)
INSERT INTO inscription (etudiant_id, cours_id) VALUES
                                                    (1, 1),
                                                    (1, 2),
                                                    (1, 3),
                                                    (2, 1),
                                                    (2, 2),
                                                    (2, 3),
                                                    (2, 4),
                                                    (2, 5),
                                                    (3, 4),
                                                    (3, 5);

-- Insert Notes
INSERT INTO note (etudiant_id, cours_id, premiere_session, deuxieme_session) VALUES
                                                                                 (1, 1, NULL, NULL),
                                                                                 (1, 2, NULL, NULL),
                                                                                 (1, 3, NULL, NULL),
                                                                                 (2, 1, NULL, NULL),
                                                                                 (2, 2, NULL, NULL),
                                                                                 (2, 3, NULL, NULL),
                                                                                 (2, 4, NULL, NULL),
                                                                                 (2, 5, NULL, NULL),
                                                                                 (3, 4, NULL, NULL),
                                                                                 (3, 5, NULL, NULL);
SELECT setval('utilisateur_id_seq', (SELECT MAX(id) FROM utilisateur));
