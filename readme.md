# ShopWise

SaaS pour commerces de proximité : **clients**, **rendez-vous** et **fidélisation** (points).
Backend Java 21 / Spring Boot · Frontend Angular 17 (responsive) · MariaDB 11.4 · API ouverte sans authentification.

## User stories → réalisation

| User story | API | Écran |
|---|---|---|
| Commerçant : créer / mettre à jour une fiche client | `POST /clients/shops/{shop_id}` · `PATCH /clients/{id}/shops/{shop_id}` | Clients (liste) |
| Commerçant : créer un rendez-vous (client, date, heure, service) | `POST /shops/{shop_id}/rendez-vous` | — |
| Commerçant : lister / filtrer les rendez-vous (date, statut, client) | `GET /shops/{shop_id}/rendez-vous?date=&statut=&client_id=` | Rendez-vous |
| Commerçant : marquer un rendez-vous honoré | `PATCH /shops/{shop_id}/rendez-vous/{id}/honorer` (409 si déjà traité) | Rendez-vous (bouton « Honorer ») |
| Points attribués automatiquement quand le rendez-vous est honoré | crédit de `Service.points` + ligne `TransactionPoints` (GAIN) | Espace client |
| Client : se connecter (compte créé par le commerçant) | `POST /auth/login` (401 si identifiants faux, mot de passe vérifié en BCrypt) | Espace client |
| Client : consulter solde et historique des points | `GET /clients/{id}/points` | Espace client |

## Lancer le projet

### Avec Docker (recommandé)
```bash
docker compose up -d --build
```
| Service | URL |
|---|---|
| Frontend (nginx) | http://localhost |
| Backend (Spring Boot) | http://localhost:8080 |
| MariaDB | localhost:3306 (`shopWise` / `dev`, base `shopwise_db`) |

La base est initialisée au premier démarrage par `src/main/resources/sql/01_schema.sql` puis `02_seed.sql` (données de démo).
nginx sert l'application Angular et redirige `/shops`, `/clients`, `/auth` vers le conteneur `backend`.

### En développement
```bash
docker compose up -d mariadb          # base seule
./mvnw spring-boot:run                # backend sur :8080
cd frontend/shopWise_front_app && npm install && npx ng serve   # front sur :4200 (proxy.conf.json -> :8080)
```

## Tests et couverture

| Partie | Commande | Résultat | Rapport |
|---|---|---|---|
| Backend (JUnit 5, Mockito, `@WebMvcTest`) | `./mvnw verify` | 45 tests · **74 % instructions · 97 % branches** | `couverture/backend/index.html` |
| Frontend (Jasmine, Karma) | `cd frontend/shopWise_front_app && npm run test:ci` | 19 tests · **100 % instructions · 100 % branches** | `couverture/frontend/index.html` |

Le front a besoin d'un Chrome : en local, `CHROME_BIN` peut pointer vers le Chromium de puppeteer
(`export CHROME_BIN=$(node -e "console.log(require('puppeteer').executablePath())")`).

## Base de données
- Schéma : `bdd/` (PDF du modèle) et script SQL.

## CI/CD (GitHub Actions)

Fichier : `.github/workflows/ci-cd.yml`, déclenché à chaque push sur `master`/`main` et à chaque pull request.

1. **backend** : `./mvnw verify` (compilation, tests, rapport JaCoCo) ; rapports publiés en artefacts.
2. **frontend** : `npm ci`, tests + couverture en Chrome headless, build de production ; rapport publié en artefact.
3. **docker** (push uniquement, si les deux jobs sont verts) : construction et publication sur DockerHub de
   `<utilisateur>/shopwise-backend` et `<utilisateur>/shopwise-frontend` (tags `latest` et SHA du commit).

Secrets à définir dans *Settings → Secrets and variables → Actions* : `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`.

## Déploiement

Sur un serveur avec Docker :
```bash
git clone <url-du-depot> && cd <depot>
DOCKERHUB_USER=<utilisateur> docker compose pull backend frontend   # images publiées par la CI
DOCKERHUB_USER=<utilisateur> docker compose up -d
```
Mise à jour : `docker compose pull && docker compose up -d` (la base est conservée dans le volume `db_data`).

## Workflow Git

Le workflow se déclenche au push sur la branche principale, lorsqu'une pull request est ouverte ou mise à jour.
Les jobs s'enchaînent : le code est testé avant que les images soient construites et publiées.
Chaque feature est développée dans une branche à part (`feature/NOM_FEATURE`) ; chaque commit suit l'historique du
développement de la feature et référence son issue (`#n`). Une fois la pull request fusionnée sur la branche principale,
le workflow publie les images.

Une issue par user story, avec labels (`backend`, `frontend`, `clients`, `rendez-vous`, `fidélité`, `ci-cd`).

## Usage de l'IA

Une partie du code a été écrite avec l'assistant **Claude (Anthropic)**, sous ma supervision et relue par moi :
commits signés `Co-Authored-By: Claude`. Concerné notamment : filtres et tests de couverture des rendez-vous,
connexion et consultation des points, écrans Angular et leurs tests, Dockerfiles, `docker-compose.yml`, workflow CI/CD.

## Limites connues
- Annulation d'un rendez-vous (`ANNULE`) : non implémentée.
- Formulaires de création/modification de client et de création de rendez-vous : disponibles par l'API, pas encore à l'écran.
