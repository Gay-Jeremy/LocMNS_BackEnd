[![Java CI with Maven](https://github.com/Gay-Jeremy/LocMNS_BackEnd/actions/workflows/ci.yml/badge.svg)](https://github.com/Gay-Jeremy/LocMNS_BackEnd/actions/workflows/ci.yml)

# LocMns - Backend

API REST de gestion du prêt de matériel de Metz Numeric School (ordinateurs, écrans, tablettes...) :
les étudiants et enseignants demandent des emprunts, les administrateurs gèrent le parc et valident les demandes.

Projet réalisé par **Jérémy Gay** dans le cadre du titre professionnel **Concepteur Développeur
d'Applications** (RNCP niveau 6), de la conception au déploiement.

- Interface web (Angular) : [LocMNS_FrontEnd](https://github.com/Gay-Jeremy/LocMNS_FrontEnd)
- Documentation de l'API : Swagger UI, une fois l'application lancée (voir plus bas)

## Fonctionnalités

- **Authentification** par jeton JWT, inscription publique (sans possibilité de s'inscrire administrateur).
- **Trois rôles** : Administrateur, Enseignant, Étudiant. Les droits d'emprunt dépendent de la catégorie
  de l'utilisateur et de la catégorie du matériel.
- **Emprunts** : demande sur une période, contrôle de disponibilité du matériel sur ces dates,
  acceptation ou refus par un administrateur, demandes de prolongation et de retour anticipé,
  suivi des emprunts actifs, en retard et de l'historique.
- **Matériel** : modèles, catégories, composants, salles, documentation, statut technique
  (dont le signalement de panne).
- **Utilisateurs** : gestion par les administrateurs ; rattachement à une promotion.

## Sécurité

- Mots de passe chiffrés avec **BCrypt**, jamais renvoyés par l'API.
- API **sans état** (*stateless*) : chaque requête porte son jeton JWT, vérifié par un filtre dédié.
- **Contrôle d'accès par rôle** sur chaque route (`@PreAuthorize`, annotations `@IsAdmin` / `@IsUser`) :
  lecture pour tout utilisateur connecté, création, modification et suppression des référentiels
  réservées aux administrateurs.
- **Contrôle d'appartenance** : un utilisateur ne voit et ne modifie que ses propres emprunts
  (404 en lecture, 403 en modification pour l'emprunt d'un autre) ; l'administrateur garde tous les droits.
- **Clé JWT** : l'application refuse de démarrer si `JWT_SECRET` fait moins de 32 caractères,
  plutôt que de signer les jetons avec une clé faible.
- **CORS** limité aux origines déclarées dans la configuration.
- **Dépendances** surveillées par Dependabot et analysées par OSV-Scanner à chaque build
  (voir [Sécurité des dépendances](#sécurité-des-dépendances)).

## Stack technique

- **Java 25** (LTS)
- **Spring Boot 4.1** : Spring Web MVC, Spring Data JPA, Spring Security, Bean Validation
- **PostgreSQL 18** (Hibernate / JPA)
- **JWT** avec jjwt 0.13
- **springdoc-openapi 3** (documentation Swagger)
- **Maven** (wrapper `mvnw` fourni)
- **JUnit 5, Mockito, Spring Security Test** (tests)
- **Docker**, **GitHub Actions** (intégration et déploiement continus)

## Architecture

Architecture en couches : `controller` → `services` → `repository`.

| Paquet | Rôle |
|---|---|
| `controller` | Points d'entrée REST (un contrôleur par ressource) |
| `services` | Règles métier (interfaces + implémentations) |
| `repository` | Accès aux données (Spring Data JPA) |
| `model` | Entités JPA |
| `dto` | Objets échangés avec le client (`request`, `response`, groupes de validation) |
| `mapper` | Conversion entité ↔ DTO |
| `security` | Filtre JWT, configuration Spring Security, annotations de rôle |
| `exception` | Gestion centralisée des erreurs (`@RestControllerAdvice`) |
| `config` | Clé JWT, encodeur de mots de passe, configuration Swagger |

## Lancer le projet en local

### Prérequis

- JDK 25 (Temurin recommandé)
- Docker (pour la base PostgreSQL)
- Maven n'est pas obligatoire : le wrapper `mvnw` le télécharge automatiquement

### Étapes

```bash
# 1. Configuration : copier le modèle puis renseigner les valeurs
cp .env.example .env

# 2. Base de données PostgreSQL
docker compose up -d postgres

# 3. Application (http://localhost:8080)
./mvnw spring-boot:run
```

Sous Windows, utiliser `mvnw.cmd` à la place de `./mvnw`. Avec `DDL_AUTO=create`, les tables sont
recréées à chaque démarrage et un jeu d'essai est chargé (`src/main/resources/data-init.sql`).

### Configuration

L'application lit sa configuration dans un fichier `.env` (non versionné), sur le modèle de `.env.example`.

| Variable | Description | Exemple (dev) |
|---|---|---|
| `DB_HOST` | Hôte de la base (`localhost` hors Docker) | `localhost` |
| `DB_PORT` | Port de la base | `5432` |
| `DB_NAME` | Nom de la base | `locmns` |
| `DB_USER` | Utilisateur de la base | `postgres` |
| `DB_PASSWORD` | Mot de passe de la base | *(à définir)* |
| `POSTGRES_DB` | Base créée par le conteneur PostgreSQL | `locmns` |
| `POSTGRES_USER` | Utilisateur créé par le conteneur PostgreSQL | `postgres` |
| `POSTGRES_PASSWORD` | Mot de passe du conteneur PostgreSQL | *(à définir)* |
| `JWT_SECRET` | Clé de signature des JWT, **32 caractères minimum** (voir ci-dessous) | *(généré)* |
| `DDL_AUTO` | Stratégie Hibernate (`create` en dev, `update` en production) | `create` |
| `SQL_FILE` | Suffixe du script de données chargé (`data-<SQL_FILE>.sql`) | `init` |
| `SQL_INIT_MODE` | Chargement du jeu d'essai (`always` ou `never`) | `always` |
| `CORS_ALLOWED_ORIGINS` | Origines autorisées, séparées par des virgules | `http://localhost:4200` |

**Clé JWT.** Générer une valeur aléatoire et la coller dans `JWT_SECRET` :

```bash
openssl rand -base64 48
```

Si `JWT_SECRET` est laissé vide, l'application génère une clé aléatoire de 256 bits au démarrage
(`SecureRandom`). Pratique en développement, mais les utilisateurs doivent se reconnecter après
chaque redémarrage : en production, toujours renseigner une valeur fixe.

> **Sécurité :** ne jamais committer le `.env`.

## Documentation de l'API (Swagger)

Une fois l'application lancée :

- Swagger UI : http://localhost:8080/swagger-ui.html
- Spécification OpenAPI : http://localhost:8080/v3/api-docs

Pour tester les routes protégées : appeler `POST /api/login`, puis coller le jeton reçu dans
le bouton **Authorize** (schéma *Bearer*).

## Tests

| Type | Nombre | Contenu |
|---|---|---|
| Unitaires (`src/test/.../unit`) | 58 | Services (emprunts, matériel, utilisateurs) et JWT, avec Mockito |
| Intégration (`src/test/.../integration`) | 135 | Contexte Spring complet et base PostgreSQL réelle, dont 118 cas de contrôle d'accès par rôle |

Les deux types de tests sont exécutés par la CI à chaque pull request.

```bash
./mvnw test       # tests unitaires
./mvnw verify     # tests unitaires + construction du jar
```

En local, les tests d'intégration nécessitent PostgreSQL (`docker compose up -d postgres`) :

```bash
./mvnw test "-Dtest=**/integration/*Test" "-Dsurefire.failIfNoSpecifiedTests=false"
```

## Intégration et déploiement continus

Pipeline GitHub Actions (`.github/workflows/ci.yml`) :

| Job | Quand | Ce qu'il fait |
|---|---|---|
| `build` | Chaque push et pull request sur `main` et `dev` | Compilation, tests unitaires, tests d'intégration (base PostgreSQL éphémère), audit de sécurité des dépendances |
| `publish` | Après un push sur `main` | Construction de l'image Docker et publication sur Docker Hub |
| `deploy` | Après `publish` | Déploiement sur le serveur (runner auto-hébergé, `docker compose`) |

Organisation des branches : chaque évolution passe par une branche dédiée et une pull request vers `dev`.
La branche `main`, qui déclenche la mise en production, est protégée : pull request et CI verte obligatoires.

Le fichier `compose.yml` décrit la pile déployée (PostgreSQL, API, frontend) à partir des images
publiées sur Docker Hub (`jeremygaydev/locmns-back`, `jeremygaydev/locmns-front`).

## Sécurité des dépendances

### Mises à jour automatiques (Dependabot)

Dependabot (`.github/dependabot.yml`) ouvre chaque lundi une pull request vers `dev` :

- les dépendances Spring Boot sont regroupées : la version de Spring Boot fixe celles de Spring,
  Tomcat, Jackson, Hibernate et du pilote PostgreSQL, qui évoluent donc ensemble ;
- seules les versions **mineures et correctives** sont proposées ; les versions majeures
  se font à la main, avec les guides de migration ;
- les actions GitHub de la CI sont aussi mises à jour (une fois par mois) ;
- la CI exécute les tests sur chaque pull request avant toute fusion.

### Contrôle dans la CI

La CI analyse le `pom.xml` avec [OSV-Scanner](https://google.github.io/osv-scanner/) (base de
vulnérabilités OSV.dev, maintenue par Google) : elle échoue si une dépendance, directe ou
transitive, présente une faille connue. Pour lancer la même analyse en local :

```bash
docker run --rm -v "${PWD}:/src:ro" -w /src ghcr.io/google/osv-scanner-action:v2.7.0 --lockfile=./pom.xml
```
