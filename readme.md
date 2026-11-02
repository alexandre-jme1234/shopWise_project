# ShopWise

SaaS pour commerces de proximité : **clients**, **rendez-vous** et **fidélisation** (points).
Backend Java 21 / Spring Boot · Frontend Angular 17 (responsive : desktop, et mobile < 600 px où les tableaux deviennent des cartes) · MariaDB 11.4 · API ouverte sans authentification.

## User stories → réalisation

| User story | API | Écran |
|---|---|---|
| Commerçant : créer / mettre à jour une fiche client | `POST /clients/shops/{shop_id}` · `PATCH /clients/{id}/shops/{shop_id}` | Clients (formulaire création / modification, liste) |
| Commerçant : créer un rendez-vous (client, date, heure, service) | `POST /shops/{shop_id}/rendez-vous` | Rendez-vous (formulaire « Nouveau rendez-vous ») |
| Commerçant : lister / filtrer les rendez-vous (date, statut, client) | `GET /shops/{shop_id}/rendez-vous?date=&statut=&client_id=` | Rendez-vous |
| Commerçant : marquer un rendez-vous honoré ou annulé | `PATCH /shops/{shop_id}/rendez-vous/{id}/honorer` · `PATCH …/{id}/annuler` (409 si déjà traité) | Rendez-vous (boutons « Honorer » / « Annuler ») |
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
| Backend (JUnit 5, Mockito, `@WebMvcTest`) | `./mvnw verify` | 52 tests · **75 % instructions · 97 % branches** | `couverture/backend/index.html` |
| Frontend (Jasmine, Karma) | `cd frontend/shopWise_front_app && npm run test:ci` | 31 tests · **100 % instructions · 100 % branches** | `couverture/frontend/index.html` |

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

## Workflow Git retenu

Modèle **GitHub Flow** : une branche principale toujours stable et déployable (`master`), et des branches courtes par fonctionnalité.

### 1. Une issue par user story
- Chaque user story est une **issue GitHub** (titre = la user story, critères d'acceptation dans la description).
- Labels : type (`feature`, `bug`, `docs`, `ci-cd`), partie (`backend`, `frontend`), module (`clients`, `rendez-vous`, `fidélité`).

### 2. Une branche par issue
```bash
git switch master && git pull
git switch -c feature/<n>-<description-courte>     # ex. feature/3-filtrer-rendez-vous
```
Préfixes : `feature/` (fonctionnalité), `fix/` (correction), `docs/`, `ci/`.

### 3. Des commits petits, liés à l'issue
Convention **Conventional Commits**, avec le numéro de l'issue :
```
<type>(<portée facultative>): <description> (#n)
feat: lister et filtrer les rendez-vous d'une boutique (#3)
feat(front): formulaire de création de rendez-vous (#2)
fix: 409 si le rendez-vous est déjà honoré (#4)
```
Types utilisés : `feat`, `fix`, `test`, `docs`, `build`, `ci`, `refactor`.

### 4. Pull request vers `master`
- `git push -u origin feature/<n>-...`, puis ouverture d'une **pull request** dont la description contient `Closes #n`
  (l'issue se ferme automatiquement à la fusion).
- La **CI** (`.github/workflows/ci-cd.yml`) s'exécute sur la pull request : build, tests back et front, couverture.
  On ne fusionne que si elle est verte.

### 5. Fusion et livraison
- Fusion dans `master`, puis suppression de la branche.
- Le push sur `master` relance la CI ; si les tests passent, le job `docker` publie les images sur DockerHub
  (`latest` et SHA du commit), prêtes pour le déploiement (voir « Déploiement »).

### Remarque
Pendant la phase finale du rendu, en développement solo, plusieurs commits ont été faits directement sur `master` :
le workflow ci-dessus est celui appliqué pour la suite du projet.

## Usage de l'IA

Une partie du code a été écrite avec l'assistant **Claude (Anthropic)**, sous ma supervision et relue par moi :
commits signés `Co-Authored-By: Claude`. Concerné notamment : filtres et tests de couverture des rendez-vous,
connexion et consultation des points, écrans Angular et leurs tests, Dockerfiles, `docker-compose.yml`, workflow CI/CD.

## Limites connues
- La boutique et le service se saisissent par leur numéro (pas encore de listes déroulantes).
- Pas de session côté front : l'espace client se déconnecte au rechargement de la page.
