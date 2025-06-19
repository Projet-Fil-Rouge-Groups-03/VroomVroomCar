-- Mettre ici les fake Datas INSERT INTO....

INSERT INTO utilisateur (nom, prenom, mail, adresse, mot_de_passe, status) VALUES
('N/A', 'admin', 'admin@admin.com', 'N/A', 'admin', 'ROLE_ADMIN'),
('Martin', 'Claire', 'claire.martin@example.com', '25 avenue de Paris', 'mdp456', 'ROLE_ACTIF'),
('Lemoine', 'Paul', 'paul.lemoine@example.com', '8 place des Fleurs', 'mdp789', 'ROLE_ACTIF'),
('Dubois', 'Sophie', 'sophie.dubois@example.com', '12 boulevard des Alpes', 'mdp234', 'ROLE_ACTIF'),
('Mechant', 'Bowser', 'koopa@nintendo.com', 'Château de Bowser, 75000 PARIS', 'peach', 'ROLE_BANNI'),
('Bernard', 'Lucie', 'lucie.bernard@example.com', '5 rue Lafayette', 'mdp567', 'ROLE_ACTIF');

INSERT INTO voiture (marque, modele, nb_places, pollution, infos_supp, utilisateur_id, motorisation, categories) VALUES
('Renault', 'Clio', 5, NULL, NULL, 1, 'ESSENCE', 'MINI_CITADINES'),
('Peugeot', '208', 5, NULL, NULL, 1, 'HYBRIDE', 'CITADINES'),
('Toyota', 'Yaris', 5, NULL, NULL, 1, 'ELECTRIQUE', 'COMPACTES'),
('Volkswagen', 'Golf', 5, NULL, NULL, 1, 'ESSENCE', 'BERLINES_TAILLE_M'),
('BMW', 'Série 1', 5, NULL, NULL, 2, 'HYBRIDE', 'BERLINES_TAILLE_M'),
('Citroën', 'C3', 5, NULL, 'Voiture urbaine confortable', 3, 'ESSENCE', 'CITADINES'),
('Renault', 'Clio', 5, NULL, NULL, 1, 'ESSENCE', 'MINI_CITADINES'),
('Hyundai', 'Ioniq 5', 5, NULL, '100% électrique', 5, 'ELECTRIQUE', 'SUV'),
('BMW', 'Série 1', 5, NULL, NULL, 2, 'HYBRIDE', 'BERLINES_TAILLE_M'),
('Opel', 'Corsa', 5, NULL, NULL, 4, 'ESSENCE', 'MINI_CITADINES');

INSERT INTO voiture_de_service (id, immatriculation, url_photo, status) VALUES
(1, 'AB-123-CD', NULL, 'EN_SERVICE'),
(2, 'EF-456-GH', NULL, 'HORS_SERVICE'),
(3, 'IJ-789-KL', NULL, 'REPARATION'),
(4, 'MN-321-OP', NULL, 'EN_SERVICE'),
(5, 'QR-654-ST', NULL, 'EN_SERVICE');

INSERT INTO réservation (user_id,voiture_de_service_id, date_début, date_fin) VALUES
(2, 1, '2025-06-20', '2025-06-21'),
(2, 2, '2025-07-01', '2025-07-03'),
(3, 3, '2025-06-22', '2025-06-24'),
(4, 4, '2025-06-25', '2025-06-27'),
(5, 5, '2025-06-28', '2025-06-30');

INSERT INTO trajet (organisateur_id, car_id, date_debut, date_fin, heure_depart, heure_arrivee, lieu_depart, ville_depart, lieu_arrivee, ville_arrivee, nb_places_restantes) VALUES
(2, 6, '2025-07-05', '2025-07-05', '08:00:00', '12:00:00', 'Gare de Lyon', 'Paris', 'Part-Dieu', 'Lyon', 3),
(3, 7, '2025-07-06', '2025-07-06', '09:30:00', '11:00:00', 'Place Rihour', 'Lille', 'Gare Centrale', 'Bruxelles', 2),
(4, 10, '2025-07-07', '2025-07-07', '07:00:00', '13:00:00', 'Université Grenoble', 'Grenoble', 'Promenade des Anglais', 'Nice', 4),
(5, 8, '2025-07-08', '2025-07-08', '10:00:00', '12:30:00', 'Place de la Victoire', 'Bordeaux', 'Capitole', 'Toulouse', 1),
(2, 9, '2025-07-09', '2025-07-09', '14:00:00', '15:45:00', 'Place Kléber', 'Strasbourg', 'Place Stanislas', 'Nancy', 3);

INSERT INTO inscription (utilisateur_id, trajet_id, date_inscription) VALUES
(1, 1, '2025-06-15'),
(2, 1, '2025-06-15'),
(3, 2, '2025-06-16'),
(4, 3, '2025-06-17'),
(5, 4, '2025-06-18');

INSERT INTO notification (user_id, date, nom, contenu) VALUES
(2, '2025-06-12', 'Confirmation de réservation', 'Votre réservation du 20 juin a été confirmée.'),
(3, '2025-06-12', 'Trajet disponible', 'Un nouveau trajet correspondant à vos préférences est disponible.'),
(4, '2025-06-11', 'Modification de trajet', 'Les horaires de votre trajet vers Nice ont été modifiés.'),
(5, '2025-06-10', 'Nouvelle voiture ajoutée', 'Une nouvelle voiture est disponible pour réservation.'),
(2, '2025-06-09', 'Rappel de trajet', 'N’oubliez pas votre trajet Paris-Lyon prévu le 5 juillet.');
