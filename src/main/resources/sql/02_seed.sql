-- =====================================================================
--  shopWise_DB — 02_seed.sql
--  Jeu de données de test (fictif, figé : même résultat à chaque exécution)
--  À exécuter APRÈS 01_schema.sql (qui vide et recrée les tables)
--  Date de référence du jeu : 19/10/2026
--    • RDV passés   → HONORE ou ANNULE (sauf 1 oubli volontaire, id 29)
--    • RDV à venir  → PLANIFIE
-- =====================================================================

START TRANSACTION;

-- ---------------------------------------------------------------------
-- 1. Magasins
-- ---------------------------------------------------------------------
INSERT INTO Shop (id, nom, city) VALUES
  (1, 'Salon Lumière',     'Lyon'),
  (2, 'Atelier Belle Vue', 'Villeurbanne');

-- ---------------------------------------------------------------------
-- 2. Utilisateurs
--    id 1-3  : commerçants
--    id 4-15 : clients
--    Mot de passe de test pour tous : « Test1234! »
--    (hash fictif : à remplacer par un vrai hash bcrypt généré par l'appli)
-- ---------------------------------------------------------------------
INSERT INTO `User` (id, name, email, phone, password_hash) VALUES
  ( 1, 'Sophie Martin',   'sophie.martin@lumiere.test',  '0611223344', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 2, 'Karim Benali',    'karim.benali@lumiere.test',   '0622334455', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 3, 'Julie Moreau',    'julie.moreau@bellevue.test',  '0633445566', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 4, 'Léa Dubois',      'lea.dubois@mail.test',        '0644556677', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 5, 'Hugo Lefèvre',    'hugo.lefevre@mail.test',      '0655667788', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 6, 'Chloé Garnier',   'chloe.garnier@mail.test',     '0666778899', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 7, 'Nathan Roux',     'nathan.roux@mail.test',       '0677889900', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 8, 'Emma Fontaine',   'emma.fontaine@mail.test',     '0688990011', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  ( 9, 'Lucas Girard',    'lucas.girard@mail.test',      '0699001122', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  (10, 'Inès Mercier',    'ines.mercier@mail.test',      '0710111213', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  (11, 'Tom Lambert',     'tom.lambert@mail.test',       '0720212223', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  (12, 'Camille Faure',   'camille.faure@mail.test',     '0730313233', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  (13, 'Yanis Chevalier', 'yanis.chevalier@mail.test',   '0740414243', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  (14, 'Manon Blanc',     'manon.blanc@mail.test',       NULL,         '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000'),
  (15, 'Arthur Perrin',   'arthur.perrin@mail.test',     '0760616263', '$2y$10$FAKEHASHFORTESTSONLY000000000000000000000000000000000');
  -- id 14 : client sans téléphone (champ facultatif)
  -- id 15 : client tout juste créé, sans aucun RDV

INSERT INTO StoreManager (user_id, shop_id) VALUES
  (1, 1),
  (2, 1),   -- deux commerçants pour le même magasin
  (3, 2);

-- solde_points à 0 : il est recalculé à partir de l'historique (étape 6)
INSERT INTO Client (user_id, solde_points, shop_id) VALUES
  (4, 0, 2), (5, 0, 1), (6, 0,  1), (7, 0,  2), (8, 0,  1), (9, 0,  2),
  (10, 0,  2), (11, 0,  1), (12, 0,  2), (13, 0,  1), (14, 0,  2), (15, 0,  2);

-- ---------------------------------------------------------------------
-- 3. Catalogue de services
-- ---------------------------------------------------------------------
INSERT INTO Service (id, shop_id, produit, prix, points) VALUES
  (1, 1, 'Coupe femme',   35.00, 15),
  (2, 1, 'Coupe homme',   22.00, 10),
  (3, 1, 'Coloration',    65.00, 30),
  (4, 1, 'Brushing',      25.00, 10),
  (5, 1, 'Soin profond',  40.00, 20),
  (6, 2, 'Coupe femme',   32.00, 15),
  (7, 2, 'Balayage',      85.00, 40),
  (8, 2, 'Taille barbe',  15.00,  5);

-- ---------------------------------------------------------------------
-- 4. Rendez-vous
-- ---------------------------------------------------------------------
INSERT INTO RendezVous (id, client_id, service_id, statut_code, date_heure) VALUES
  -- Passés
  ( 1,  4, 1, 'HONORE',   '2026-09-02 10:00:00'),
  ( 2,  4, 3, 'HONORE',   '2026-09-23 14:30:00'),
  ( 3,  4, 4, 'HONORE',   '2026-10-10 11:00:00'),
  ( 4,  5, 2, 'HONORE',   '2026-09-05 09:30:00'),
  ( 5,  5, 2, 'ANNULE',   '2026-10-03 09:30:00'),
  ( 6,  6, 3, 'HONORE',   '2026-09-12 15:00:00'),
  ( 7,  6, 5, 'HONORE',   '2026-10-08 16:00:00'),
  ( 8,  7, 2, 'ANNULE',   '2026-09-18 18:00:00'),
  ( 9,  8, 1, 'HONORE',   '2026-09-20 10:30:00'),
  (10,  8, 4, 'HONORE',   '2026-10-14 17:00:00'),
  (11,  9, 8, 'HONORE',   '2026-09-26 12:00:00'),
  (12,  9, 8, 'HONORE',   '2026-10-11 12:00:00'),
  (13, 10, 7, 'HONORE',   '2026-09-30 14:00:00'),
  (14, 11, 6, 'ANNULE',   '2026-10-06 10:00:00'),
  (15, 12, 6, 'HONORE',   '2026-10-02 11:30:00'),
  (16, 12, 7, 'HONORE',   '2026-10-16 15:30:00'),
  (17, 13, 2, 'HONORE',   '2026-10-01 18:30:00'),
  (18, 14, 1, 'ANNULE',   '2026-10-15 09:00:00'),
  -- À venir
  (19,  4, 1, 'PLANIFIE', '2026-10-22 10:00:00'),
  (20,  5, 2, 'PLANIFIE', '2026-10-21 09:30:00'),
  (21,  6, 3, 'PLANIFIE', '2026-11-04 15:00:00'),
  (22,  7, 2, 'PLANIFIE', '2026-10-24 18:00:00'),
  (23,  8, 5, 'PLANIFIE', '2026-10-28 16:30:00'),
  (24, 10, 7, 'PLANIFIE', '2026-11-12 14:00:00'),
  (25, 11, 6, 'PLANIFIE', '2026-10-20 10:00:00'),
  (26, 13, 4, 'PLANIFIE', '2026-10-23 17:00:00'),
  (27, 14, 1, 'PLANIFIE', '2026-10-30 09:00:00'),
  (28,  9, 8, 'PLANIFIE', '2026-10-25 12:00:00'),
  -- Cas limite : RDV passé que le commerçant a oublié de mettre à jour
  (29,  7, 4, 'PLANIFIE', '2026-10-17 11:00:00');

-- ---------------------------------------------------------------------
-- 5. Historique de fidélité
-- 5a. GAIN : une transaction par RDV honoré, points = points du service
-- ---------------------------------------------------------------------
INSERT INTO TransactionPoints (client_id, rendez_vous_id, type, points, date_transaction)
SELECT r.client_id, r.id, 'GAIN', s.points, r.date_heure
FROM RendezVous r
JOIN Service s ON s.id = r.service_id
WHERE r.statut_code = 'HONORE'
ORDER BY r.id;

-- 5b. UTILISATION : points dépensés (pas de RDV associé → NULL)
INSERT INTO TransactionPoints (client_id, rendez_vous_id, type, points, date_transaction) VALUES
  ( 4, NULL, 'UTILISATION', 30, '2026-10-12 18:00:00'),
  ( 6, NULL, 'UTILISATION', 20, '2026-10-09 10:00:00'),
  (12, NULL, 'UTILISATION', 40, '2026-10-17 09:00:00');

-- ---------------------------------------------------------------------
-- 6. Solde = somme des GAIN - somme des UTILISATION
-- ---------------------------------------------------------------------
UPDATE Client c
SET c.solde_points = (
  SELECT COALESCE(SUM(CASE WHEN t.type = 'GAIN' THEN t.points ELSE -t.points END), 0)
  FROM TransactionPoints t
  WHERE t.client_id = c.user_id
);

-- ---------------------------------------------------------------------
-- 7. Facturation : une facture par RDV honoré, au prix du service
-- ---------------------------------------------------------------------
INSERT INTO Facturation (client_id, service_id, date_facturation, nom, montant)
SELECT r.client_id, r.service_id, r.date_heure, s.produit, s.prix
FROM RendezVous r
JOIN Service s ON s.id = r.service_id
WHERE r.statut_code = 'HONORE'
ORDER BY r.id;

COMMIT;

-- =====================================================================
--  Requêtes de vérification (une par user story)
-- =====================================================================

-- Liste des RDV d'une date donnée
-- SELECT * FROM RendezVous WHERE DATE(date_heure) = '2026-10-22';

-- RDV filtrés par statut
-- SELECT * FROM RendezVous WHERE statut_code = 'ANNULE';

-- RDV d'un client
-- SELECT r.*, s.produit FROM RendezVous r JOIN Service s ON s.id = r.service_id
-- WHERE r.client_id = 4 ORDER BY r.date_heure;

-- Solde et historique d'un client
-- SELECT u.name, c.solde_points FROM Client c JOIN `User` u ON u.id = c.user_id WHERE c.user_id = 4;
-- SELECT type, points, date_transaction, rendez_vous_id
-- FROM TransactionPoints WHERE client_id = 4 ORDER BY date_transaction;

-- Contrôle de cohérence : doit renvoyer 0 ligne
-- SELECT c.user_id, c.solde_points,
--        COALESCE(SUM(CASE WHEN t.type='GAIN' THEN t.points ELSE -t.points END),0) AS calcul
-- FROM Client c LEFT JOIN TransactionPoints t ON t.client_id = c.user_id
-- GROUP BY c.user_id, c.solde_points
-- HAVING c.solde_points <> calcul;

-- RDV passés encore « PLANIFIE » (doit trouver le n° 29)
-- SELECT * FROM RendezVous WHERE statut_code = 'PLANIFIE' AND date_heure < NOW();
