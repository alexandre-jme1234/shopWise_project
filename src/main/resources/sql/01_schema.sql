-- =====================================================================
--  shopWise_DB — 01_schema.sql
--  Modules : Clients, Fidélisation, Rendez-vous
--  Héritage User -> Client / StoreManager : stratégie « une table par classe »
--  Cible : MariaDB 10.5+ (CHECK, InnoDB)
-- =====================================================================

-- Repartir d'une base propre (ordre inverse des dépendances)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS Facturation;
DROP TABLE IF EXISTS TransactionPoints;
DROP TABLE IF EXISTS RendezVous;
DROP TABLE IF EXISTS Service;
DROP TABLE IF EXISTS Statut;
DROP TABLE IF EXISTS StoreManager;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS Shop;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- Shop : le magasin
-- ---------------------------------------------------------------------
CREATE TABLE Shop (
  id    INT          NOT NULL AUTO_INCREMENT,
  nom   VARCHAR(100) NOT NULL,
  city  VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- User : classe mère (champs communs + connexion)
-- ---------------------------------------------------------------------
CREATE TABLE `User` (
  id             INT          NOT NULL AUTO_INCREMENT,
  name           VARCHAR(100) NOT NULL,
  email          VARCHAR(150) NOT NULL,
  phone          VARCHAR(20)  NULL,
  password_hash  VARCHAR(255) NOT NULL,            -- jamais le mot de passe en clair
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uq_user_email UNIQUE (email)          -- l'email sert d'identifiant de connexion
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Client : sous-classe de User (PK = FK)
-- ---------------------------------------------------------------------
CREATE TABLE Client (
  user_id       INT NOT NULL,
  solde_points  INT NOT NULL DEFAULT 0,
  PRIMARY KEY (user_id),
  CONSTRAINT fk_client_user FOREIGN KEY (user_id)
    REFERENCES `User` (id) ON DELETE CASCADE,
  CONSTRAINT chk_client_solde CHECK (solde_points >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- StoreManager : sous-classe de User, rattachée à un magasin
-- Shop 1 —— 0..* StoreManager
-- ---------------------------------------------------------------------
CREATE TABLE StoreManager (
  user_id  INT NOT NULL,
  shop_id  INT NOT NULL,
  PRIMARY KEY (user_id),
  CONSTRAINT fk_manager_user FOREIGN KEY (user_id)
    REFERENCES `User` (id) ON DELETE CASCADE,
  CONSTRAINT fk_manager_shop FOREIGN KEY (shop_id)
    REFERENCES Shop (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Statut : table de référence (remplace l'énumération)
-- ---------------------------------------------------------------------
CREATE TABLE Statut (
  code     VARCHAR(20) NOT NULL,
  libelle  VARCHAR(50) NOT NULL,
  PRIMARY KEY (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO Statut (code, libelle) VALUES
  ('PLANIFIE', 'Planifié'),
  ('HONORE',   'Honoré'),
  ('ANNULE',   'Annulé');

-- ---------------------------------------------------------------------
-- Service : catalogue du magasin
-- Shop 1 —— 0..* Service
-- ---------------------------------------------------------------------
CREATE TABLE Service (
  id       INT            NOT NULL AUTO_INCREMENT,
  shop_id  INT            NOT NULL,
  produit  VARCHAR(100)   NOT NULL,
  prix     DECIMAL(10,2)  NOT NULL,                -- DECIMAL et non FLOAT pour de l'argent
  points   INT            NOT NULL DEFAULT 0,      -- points gagnés quand le RDV est honoré
  PRIMARY KEY (id),
  CONSTRAINT fk_service_shop FOREIGN KEY (shop_id)
    REFERENCES Shop (id),
  CONSTRAINT chk_service_prix   CHECK (prix >= 0),
  CONSTRAINT chk_service_points CHECK (points >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- RendezVous
-- Client 1 —— 0..* RendezVous
-- Service 1 —— 0..* RendezVous
-- Statut 1 —— 0..* RendezVous
-- ---------------------------------------------------------------------
CREATE TABLE RendezVous (
  id           INT          NOT NULL AUTO_INCREMENT,
  client_id    INT          NOT NULL,
  service_id   INT          NOT NULL,
  statut_code  VARCHAR(20)  NOT NULL DEFAULT 'PLANIFIE',
  date_heure   DATETIME     NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_rdv_client FOREIGN KEY (client_id)
    REFERENCES Client (user_id),                   -- seul un client peut avoir un RDV
  CONSTRAINT fk_rdv_service FOREIGN KEY (service_id)
    REFERENCES Service (id),
  CONSTRAINT fk_rdv_statut FOREIGN KEY (statut_code)
    REFERENCES Statut (code),
  INDEX idx_rdv_date (date_heure)                  -- filtre « par date » de la user story
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- TransactionPoints : historique de fidélité (source de vérité du solde)
-- Client 1 —— 0..* TransactionPoints
-- RendezVous 0..1 —— 0..1 TransactionPoints
--   (rendez_vous_id NULL pour une UTILISATION de points)
-- ---------------------------------------------------------------------
CREATE TABLE TransactionPoints (
  id                INT                          NOT NULL AUTO_INCREMENT,
  client_id         INT                          NOT NULL,
  rendez_vous_id    INT                          NULL,
  type              ENUM('GAIN', 'UTILISATION')  NOT NULL,
  points            INT                          NOT NULL,
  date_transaction  DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_tp_client FOREIGN KEY (client_id)
    REFERENCES Client (user_id),
  CONSTRAINT fk_tp_rdv FOREIGN KEY (rendez_vous_id)
    REFERENCES RendezVous (id),
  CONSTRAINT uq_tp_rdv UNIQUE (rendez_vous_id),    -- un RDV ne rapporte des points qu'une fois
  CONSTRAINT chk_tp_points CHECK (points > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Facturation (hors user stories : conservée car présente dans le diagramme)
-- Client 1 —— 0..* Facturation
-- Service 1 —— 0..* Facturation
-- ---------------------------------------------------------------------
CREATE TABLE Facturation (
  id                INT            NOT NULL AUTO_INCREMENT,
  client_id         INT            NOT NULL,
  service_id        INT            NOT NULL,
  date_facturation  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  nom               VARCHAR(100)   NULL,
  montant           DECIMAL(10,2)  NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_fact_client FOREIGN KEY (client_id)
    REFERENCES Client (user_id),
  CONSTRAINT fk_fact_service FOREIGN KEY (service_id)
    REFERENCES Service (id),
  CONSTRAINT chk_fact_montant CHECK (montant >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
