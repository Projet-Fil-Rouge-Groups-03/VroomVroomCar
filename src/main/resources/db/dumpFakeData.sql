-- Mettre ici les fake Datas INSERT INTO....

INSERT INTO utilisateur (nom, prenom, mail, libelle, code_postal, ville, mot_de_passe, status) VALUES
('N/A', 'admin', 'Ehp/CufMDqEETmk0W8NBxQ==', 'RtKeSxGXVZRI3iR3Hve7EQ==', 'L2Y66RiXb3ihPOSaKy1Kyw==', 'RtKeSxGXVZRI3iR3Hve7EQ==', '$2a$12$7XDCrj6UXW7i/8OSKqWSt.Pbl0SQGnJ9KPEsHc7NByogkZdvdFwkO', 'ROLE_ADMIN'),
('Martin', 'Claire', 'dDPHgxzsQWGSM9s0FXgu/mOvl/Ub+L7MfWNLwFKVRI0=', 'scznyp4p03MIVQJ81yTu7kPBm3buYbkfb2ozu5c2tSk=', 'dH5fgCYNbb+x+JgYTUS+ZA==', 'vtRyOEFXv2s1zcUnP6m8Mg==', '$2a$12$PBvoiF5nQBN1o8zgJ0mAQeIXHygyAD3AEH9lBmhzXlxet.Bg2wC6O', 'ROLE_ACTIF'),
('Lemoine', 'Paul', 'D18d9UpTL7Y4rHPGTHvJERNWhNKV60f7Kj2hZMeoarc=', 'AVibhdp5J0nVZwf6ksFHpIuKdsyBfvdrlK4UrevmENU=','dH5fgCYNbb+x+JgYTUS+ZA==', 'vtRyOEFXv2s1zcUnP6m8Mg==', '$2a$12$YK0aXlPMEZnbB5gEeE4vw.V6Mfocn3PtNj2bpUQ.f9T1GhBkBB0mW', 'ROLE_ACTIF'),
('Dubois', 'Sophie', 'o0DPYWeozxW3HwTDTICNO4w93LDa390tJ36NmHoMt9A=', 'i8Dpm8Y2/BIMc5wJBu5dBT0QpHu9HYtUz0KOygEVmqE=','dH5fgCYNbb+x+JgYTUS+ZA==', 'vtRyOEFXv2s1zcUnP6m8Mg==', '$2a$12$CjXVOKcFD3Et/DCTUL4yBu60l7C3H4aoYR9D8NMxKmbtfKRiHIfIe', 'ROLE_ACTIF'),
('Mechant', 'Bowser', 'QM+0uCjCjX2F2Rmyj6pV6QMGpNtWWFYpqcd3d+QAylo=', 'oPWhD/JAapJDW5tX+5ydxg5Cvjxv1UygeEvNROuk+VA=', 'UuY8Lpq75FqzYt3qdGb02g==', 'IyJlQAtitZ0S4tJp/xjA5Q==', '$2a$12$ZP3I48bBARIjcJ2FN4rqvOqWBO542djMdwJGdlmeGpO26Ig4BiPJe', 'ROLE_BANNI'),
('Bernard', 'Lucie', 'MwbpaIG1PF2JiVkVnvrVMMggFvqE69BuAr/KOhKXNjc=', 'zxjRQxhva7XiP6c+/KQ/kQ==','dH5fgCYNbb+x+JgYTUS+ZA==', 'vtRyOEFXv2s1zcUnP6m8Mg==', '$2a$12$ZiuuqwsAft2CgyRLi5c57ed7n20sICN2Uh0eLAsF5cMlo4.23afAi', 'ROLE_ACTIF');

INSERT INTO voiture (marque, modele, nb_places, pollution, infos_supp, utilisateur_id, motorisation, categories) VALUES
('Renault', 'Clio', 5, 120, NULL, 1, 'ESSENCE', 'MINI_CITADINES'),
('Peugeot', '208', 5, 80, NULL, 1, 'HYBRIDE', 'CITADINES'),
('Toyota', 'Yaris', 5, 0, NULL, 1, 'ELECTRIQUE', 'COMPACTES'),
('Volkswagen', 'Golf', 5, 120, NULL, 1, 'ESSENCE', 'BERLINES_TAILLE_M'),
('BMW', 'Série 1', 5, 80, NULL, 2, 'HYBRIDE', 'BERLINES_TAILLE_M'),
('Citroën', 'C3', 5, 120, 'Voiture urbaine confortable', 3, 'ESSENCE', 'CITADINES'),
('Renault', 'Clio', 5, 120, NULL, 1, 'ESSENCE', 'MINI_CITADINES'),
('Hyundai', 'Ioniq 5', 5, 0, '100% électrique', 5, 'ELECTRIQUE', 'SUV'),
('BMW', 'Série 1', 5, 80, NULL, 2, 'HYBRIDE', 'BERLINES_TAILLE_M'),
('Opel', 'Corsa', 5, 120, NULL, 4, 'ESSENCE', 'MINI_CITADINES'),
('Toyota', 'Yaris', 5, 0, NULL, 1, 'HYBRIDE', 'COMPACTES');

INSERT INTO voiture_de_service (id, immatriculation, url_photo, status) VALUES
(1, 'AB-123-CD', 'https://images.caradisiac.com/logos-ref/modele/modele--renault-clio-5/S7-modele--renault-clio-5.jpg', 'EN_SERVICE'),
(2, 'EF-456-GH', 'https://images.caradisiac.com/logos-ref/modele/modele--peugeot-e-208-2e-generation/S7-modele--peugeot-e-208-2e-generation.jpg', 'HORS_SERVICE'),
(3, 'IJ-789-KL', 'https://images.caradisiac.com/logos-ref/modele/modele--toyota-yaris-3/S7-modele--toyota-yaris-3.jpg', 'REPARATION'),
(4, 'MN-321-OP', 'https://images.caradisiac.com/logos-ref/modele/modele--volkswagen-golf-8/S7-modele--volkswagen-golf-8.jpg', 'EN_SERVICE'),
(5, 'QR-654-ST', 'https://images.caradisiac.com/logos-ref/modele/modele--bmw-serie-1-f21-m-3-portes/S7-modele--bmw-serie-1-f21-m-3-portes.jpg', 'EN_SERVICE'),
(11, 'CK-965-MY', 'https://images.caradisiac.com/photo/7/3/8/5/147385/S0-s0-essai-video-toyota-yaris-hybride-2020-l-economie-sans-l-ennui-638614-147385.jpg', 'EN_SERVICE');

INSERT INTO réservation (user_id,voiture_de_service_id, date_début, date_fin) VALUES
(2, 1, '2025-06-20', '2025-06-21'),
(2, 2, '2025-07-01', '2025-07-03'),
(3, 3, '2025-06-22', '2025-06-24'),
(4, 4, '2025-10-25', '2025-10-27'),
(5, 5, '2025-06-28', '2025-06-30'),
(2, 1, '2025-08-01', '2025-08-03'),
(3, 2, '2025-08-05', '2025-08-07'),
(4, 3, '2025-08-10', '2025-08-12'),
(5, 4, '2025-08-15', '2025-08-17'),
(2, 5, '2025-08-20', '2025-08-22');

INSERT INTO trajet (organisateur_id, car_id, date_debut, date_fin, heure_depart, heure_arrivee, lieu_depart, ville_depart, lieu_arrivee, ville_arrivee, nb_places_restantes) VALUES
(2, 6, '2025-08-05', '2025-08-05', '08:00:00', '12:00:00', 'Gare de Lyon', 'Paris', 'Part-Dieu', 'Lyon', 3),
(3, 7, '2025-09-06', '2025-09-06', '09:30:00', '11:00:00', 'Place Rihour', 'Lille', 'Gare Centrale', 'Bruxelles', 2),
(4, 10, '2025-07-22', '2025-07-24', '07:00:00', '13:00:00', 'Université Grenoble', 'Grenoble', 'Promenade des Anglais', 'Nice', 4),
(5, 8, '2025-07-28', '2025-07-30', '10:00:00', '12:30:00', 'Place de la Victoire', 'Bordeaux', 'Capitole', 'Toulouse', 1),
(2, 9, '2025-08-09', '2025-08-09', '14:00:00', '15:45:00', 'Place Kléber', 'Strasbourg', 'Place Stanislas', 'Nancy', 3),
(1, 1, '2025-07-10', '2025-07-11', '08:30:00', '12:30:00', 'Gare Saint-Charles', 'Marseille', 'Gare de Lyon Part-Dieu', 'Lyon', 2),
(2, 3, '2025-10-11', '2025-10-12', '09:00:00', '14:00:00', 'Place de Jaude', 'Clermont-Fd', 'Place Masséna', 'Nice', 4),
(4, 4, '2025-08-12', '2025-08-13', '07:00:00', '11:00:00', 'Gare de Rennes', 'Rennes', 'Gare de Nantes', 'Nantes', 1),
(3, 5, '2025-07-13', '2025-07-14', '06:30:00', '10:30:00', 'Université de Tours', 'Tours', 'Gare de Bordeaux', 'Bordeaux', 3),
(5, 2, '2025-08-14', '2025-08-15', '10:00:00', '15:00:00', 'Gare de Dijon', 'Dijon', 'Gare de Strasbourg', 'Strasbourg', 2),
(2, 6, '2025-09-01', '2025-09-01', '08:00:00', '12:00:00', 'Gare de Lyon', 'Paris', 'Part-Dieu', 'Lyon', 3),
(3, 7, '2025-09-05', '2025-09-05', '09:30:00', '11:00:00', 'Place Rihour', 'Lille', 'Gare Centrale', 'Bruxelles', 2),
(4, 10, '2025-09-10', '2025-09-12', '07:00:00', '13:00:00', 'Université Grenoble', 'Grenoble', 'Promenade des Anglais', 'Nice', 4),
(5, 8, '2025-09-15', '2025-09-17', '10:00:00', '12:30:00', 'Place de la Victoire', 'Bordeaux', 'Capitole', 'Toulouse', 1),
(2, 9, '2025-09-20', '2025-09-20', '14:00:00', '15:45:00', 'Place Kléber', 'Strasbourg', 'Place Stanislas', 'Nancy', 3);

INSERT INTO inscription (utilisateur_id, trajet_id, date_inscription) VALUES
(1, 1, '2025-06-15'),
(2, 1, '2025-06-15'),
(3, 2, '2025-06-16'),
(4, 3, '2025-06-17'),
(5, 4, '2025-06-18'),
(1, 6, '2025-07-20'),
(2, 6, '2025-07-20'),
(3, 7, '2025-07-21'),
(4, 8, '2025-07-22'),
(5, 9, '2025-07-23');

INSERT INTO notification (user_id, date, nom, contenu) VALUES
(2, '2025-06-12', 'Confirmation de réservation', 'Votre réservation du 20 juin a été confirmée.'),
(3, '2025-06-12', 'Trajet disponible', 'Un nouveau trajet correspondant à vos préférences est disponible.'),
(4, '2025-06-11', 'Modification de trajet', 'Les horaires de votre trajet vers Nice ont été modifiés.'),
(5, '2025-06-10', 'Nouvelle voiture ajoutée', 'Une nouvelle voiture est disponible pour réservation.'),
(2, '2025-06-09', 'Rappel de trajet', 'N’oubliez pas votre trajet Paris-Lyon prévu le 5 juillet.');
