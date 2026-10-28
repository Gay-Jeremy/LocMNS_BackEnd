[![Java CI with Maven](https://github.com/Gay-Jeremy/LocMNS_BackEnd/actions/workflows/ci.yml/badge.svg)](https://github.com/Gay-Jeremy/LocMNS_BackEnd/actions/workflows/ci.yml)

# LocMns — Backend

API REST de gestion du parc de matériel empruntable de Metz Numeric School (LocMns).
Elle gère les utilisateurs, le matériel, les emprunts et leur cycle de vie, avec authentification et contrôle d'accès par rôle.

## Stack technique

- **Java 25** (LTS)
- **Spring Boot 4.0.5** (Spring Web, Spring Data JPA, Spring Security, Spring Validation)
- **PostgreSQL** (via Hibernate / JPA)
- **Maven** (gestion des dépendances et build)
- **JWT** (authentification stateless)
- **springdoc-openapi** (documentation Swagger)
- **Docker / Docker Compose** (conteneurisation et déploiement)

## Architecture

Architecture multicouche : `Controller` → `Service` → `Repository`, complétée par des DTO,
des mappers et des validateurs pour isoler les responsabilités.

## Prérequis

- JDK 25 (Temurin recommandé)
- Maven 3.9+
- Docker et Docker Compose (pour un lancement conteneurisé)
- Une base PostgreSQL (fournie par le `compose.yml` en local)

## Configuration

L'application lit sa configuration depuis un fichier `.env` (non versionné).
Un fichier `.env.example` est fourni comme modèle : copiez-le et renseignez vos valeurs.

```bash
cp .env.example .env
```

Variables attendues :

| Variable        | Description                                | Exemple (dev) |
|-----------------|--------------------------------------------|---------------|
| `DB_HOST`       | Hôte de la base                            | `localhost`   |
| `DB_PORT`       | Port de la base                            | `5432`        |
| `DB_NAME`       | Nom de la base                             | `locmns`      |
| `DB_USER`       | Utilisateur de la base                     | `postgres`    |
| `DB_PASSWORD`   | Mot de passe de la base                    | *(à définir)* |
| `JWT_SECRET`    | Clé de signature des JWT, **32 caractères minimum** (voir ci-dessous) | *(généré)* |
| `DDL_AUTO`      | Stratégie Hibernate (`create` en dev)      | `create`      |
| `SQL_FILE`      | Plateforme d'initialisation SQL            | `init`        |
| `SQL_INIT_MODE` | Mode d'exécution du jeu d'essai (`always`) | `always`      |

### Clé JWT

Générez une valeur aléatoire et collez-la dans `JWT_SECRET` :

```bash
openssl rand -base64 48
```

Si `JWT_SECRET` est laissé vide, l'application génère elle-même une clé aléatoire au démarrage
(`SecureRandom`, 256 bits). Pratique en développement, mais les utilisateurs devront se reconnecter
après chaque redémarrage : en production, renseignez toujours une valeur fixe.

> **Sécurité :** ne committez jamais votre `.env`. En production, utilisez un `JWT_SECRET`
> à forte entropie (jamais une valeur triviale) et `DDL_AUTO=update`.

## Lancer le projet

### Toute la pile avec Docker Compose (recommandé)

Le fichier `compose.yml` orchestre trois conteneurs : PostgreSQL, l'API et le frontend.

```bash
cp .env.example .env   # puis renseigner les valeurs
docker compose up -d
```

- API : http://localhost:8080
- Base PostgreSQL : port 5432

### En local (développement)

```bash
mvn clean package
java -jar target/app.jar
```

L'application démarre sur le port **8080**.

## Documentation de l'API (Swagger)

Une fois l'application lancée, la documentation interactive est disponible ici :

- Swagger UI : http://localhost:8080/swagger-ui.html
- Spécification OpenAPI : http://localhost:8080/v3/api-docs

L'authentification se fait par jeton JWT (schéma *Bearer*).

## Tests

```bash
mvn test              # tests unitaires
mvn verify            # tests unitaires + build
```

Les tests d'intégration (contexte Spring complet + base de données) sont exécutés
en local ; la pipeline d'intégration continue exécute les tests unitaires.

## Déploiement

Le déploiement est automatisé via GitHub Actions : à chaque push sur `main`, la pipeline
compile, exécute les tests unitaires, construit l'image Docker, la publie sur Docker Hub,
puis déploie la pile sur le serveur cible.
