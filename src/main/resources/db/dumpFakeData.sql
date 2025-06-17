-- Mettre ici les fake Datas INSERT INTO....

INSERT INTO utilisateur (id, nom, prenom, mail, adresse, mot_de_passe, status) VALUES
(1, '', 'admin', 'admin@admin.com', '', 'admin', 'ADMIN'),
(2, 'Martin', 'Claire', 'claire.martin@example.com', '25 avenue de Paris', 'mdp456', 'ACTIF'),
(3, 'Lemoine', 'Paul', 'paul.lemoine@example.com', '8 place des Fleurs', 'mdp789', 'ACTIF'),
(4, 'Dubois', 'Sophie', 'sophie.dubois@example.com', '12 boulevard des Alpes', 'mdp234', 'ACTIF'),
(5, 'Bernard', 'Lucie', 'lucie.bernard@example.com', '5 rue Lafayette', 'mdp567', 'ACTIF');

INSERT INTO voiture (id, marque, modele, nb_places, pollution, infos_supp, utilisateur_id, motorisation, categories) VALUES
(1, 'Renault', 'Clio', 5, 'Euro 6', NULL, 1, 'ESSENCE', 'MINI-CITADINES'),
(2, 'Peugeot', '208', 5, 'Euro 6', NULL, 1, 'HYBRIDE', 'CITADINES'),
(3, 'Toyota', 'Yaris', 5, 'Euro 5', NULL, 1, 'ELECTRIQUE', 'COMPACTES'),
(4, 'Volkswagen', 'Golf', 5, 'Euro 6', NULL, 1, 'ESSENCE', 'BERLINES TAILLE M'),
(5, 'Dacia', 'Duster', 5, 'Euro 6', NULL, 1, 'DIESEL', 'SUV'),
(6, 'Citroën', 'C3', 5, 'Euro 6d', 'Voiture urbaine confortable', 3, 'ESSENCE', 'CITADINES'),
(7, 'Ford', 'Focus', 5, 'Euro 6d', NULL, 2, 'DIESEL', 'COMPACTES'),
(8, 'Hyundai', 'Ioniq 5', 5, 'Zero Emission', '100% électrique', 5, 'ELECTRIQUE', 'SUV'),
(9, 'BMW', 'Série 1', 5, 'Euro 6', NULL, 2, 'HYBRIDE', 'BERLINES TAILLE M'),
(10, 'Opel', 'Corsa', 5, 'Euro 5', NULL, 4, 'ESSENCE', 'MINI-CITADINES');

INSERT INTO voiture_de_service (id, immatriculation, url_photo, status) VALUES
(1, 'AB-123-CD', NULL, 'EN_SERVICE'),
(2, 'EF-456-GH', NULL, 'HORS_SERVICE'),
(3, 'IJ-789-KL', NULL, 'REPARATION'),
(4, 'MN-321-OP', NULL, 'EN_SERVICE'),
(5, 'QR-654-ST', NULL, 'EN_SERVICE');

INSERT INTO réservation (id, user_id,voiture_de_service_id, date_début, date_fin) VALUES
(1, 2, 1, '2025-06-20', '2025-06-21'),
(2, 2, 2, '2025-07-01', '2025-07-03'),
(3, 3, 3, '2025-06-22', '2025-06-24'),
(4, 4, 4, '2025-06-25', '2025-06-27'),
(5, 5, 5, '2025-06-28', '2025-06-30');

INSERT INTO trajet (id, organisateur_id, car_id, date_debut, date_fin, heure_depart, heure_arrivee, lieu_depart, ville_depart, lieu_arrivee, ville_arrivee, nb_places_restantes) VALUES
(1, 2, 6, '2025-07-05', '2025-07-05', '08:00:00', '12:00:00', 'Gare de Lyon', 'Paris', 'Part-Dieu', 'Lyon', 3),
(2, 3, 7, '2025-07-06', '2025-07-06', '09:30:00', '11:00:00', 'Place Rihour', 'Lille', 'Gare Centrale', 'Bruxelles', 2),
(3, 4, 10, '2025-07-07', '2025-07-07', '07:00:00', '13:00:00', 'Université Grenoble', 'Grenoble', 'Promenade des Anglais', 'Nice', 4),
(4, 5, 8, '2025-07-08', '2025-07-08', '10:00:00', '12:30:00', 'Place de la Victoire', 'Bordeaux', 'Capitole', 'Toulouse', 1),
(5, 2, 9, '2025-07-09', '2025-07-09', '14:00:00', '15:45:00', 'Place Kléber', 'Strasbourg', 'Place Stanislas', 'Nancy', 3);

INSERT INTO inscription (utilisateur_id, trajet_id, date_inscription) VALUES
(1, 1, '2025-06-15'),
(2, 1, '2025-06-15'),
(3, 2, '2025-06-16'),
(4, 3, '2025-06-17'),
(5, 4, '2025-06-18');

INSERT INTO notification (id, user_id, date, nom, contenu) VALUES
(1, 2, '2025-06-12', 'Confirmation de réservation', 'Votre réservation du 20 juin a été confirmée.'),
(2, 3, '2025-06-12', 'Trajet disponible', 'Un nouveau trajet correspondant à vos préférences est disponible.'),
(3, 4, '2025-06-11', 'Modification de trajet', 'Les horaires de votre trajet vers Nice ont été modifiés.'),
(4, 5, '2025-06-10', 'Nouvelle voiture ajoutée', 'Une nouvelle voiture est disponible pour réservation.'),
(5, 2, '2025-06-09', 'Rappel de trajet', 'N’oubliez pas votre trajet Paris-Lyon prévu le 5 juillet.');
