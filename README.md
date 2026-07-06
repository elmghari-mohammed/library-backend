# Bibliothèque GraphQL — Library Backend

API GraphQL de gestion de bibliothèque construite avec **Java 21**, **Spring Boot 4.1.0**, **Spring Data JPA** et **H2**.

---

## Stack

| Couche | Technologie |
|--------|-------------|
| Langage | Java 21 |
| Framework | Spring Boot 4.1.0 (Spring Framework 7.x) |
| API | spring-graphql (GraphQL over HTTP) |
| ORM | Spring Data JPA + Hibernate 7 |
| Base de données | H2 (mémoire, dev) |
| Build | Maven |

---

## Architecture

```
┌──────────────────────┐
│   GraphQL Schema     │  schema.graphqls
├──────────────────────┤
│   GraphQL Controllers│  @QueryMapping / @MutationMapping
├──────────────────────┤
│   Services           │  Logique métier + @Transactional
├──────────────────────┤
│   Mappers            │  DTO → Entité / Entité → DTO (statiques)
├──────────────────────┤
│   Repositories       │  Spring Data JPA
├──────────────────────┤
│   JPA Entities       │  Author, Book, Member, Loan
└──────────────────────┘
```

### Domaines

| Entité | Rôle |
|--------|------|
| **Author** | Auteur (nom, nationalité, année de naissance) |
| **Book** | Livre (titre, ISBN, catégorie, disponibilité) lié à un auteur |
| **Member** | Membre (nom, email, téléphone) |
| **Loan** | Emprunt (livre, membre, dates) |

---

## Prérequis

- Java 21+
- Maven (ou utiliser `./mvnw` fourni)

---

## Démarrage

```bash
# Cloner
git clone https://github.com/elmghari-mohammed/library-backend.git
cd library-backend

# Lancer (profile dev par défaut)
./mvnw spring-boot:run
```

L'application démarre sur `http://localhost:8080`.

---

## Endpoints

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `POST /biblio` | HTTP | API GraphQL |
| `GET /graphiql` | HTTP | IDE GraphiQL (dev) |
| `GET /h2-console` | HTTP | Console H2 (dev) |

### Exemple d'appel

```bash
curl -s -X POST http://localhost:8080/biblio \
  -H "Content-Type: application/json" \
  -d '{"query":"{ allAuthors { id name nationality } }"}'
```

---

## Profiles Spring

| Profile | Activation | Usage |
|---------|------------|-------|
| `dev` (défaut) | `./mvnw spring-boot:run` | SQL visible, H2 console, données seedées |
| `prod` | `./mvnw spring-boot:run -Dspring-boot.run.profiles=prod` | SQL désactivé, GraphiQL désactivé |

En `dev`, le `data-dev.sql` insère 3 auteurs (Hugo, Orwell, Rowling), 5 livres et 2 membres.

---

## Variables d'environnement

Copier `.env.example` en `.env` :

```
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:h2:mem:bibliotheque
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=
```

---

## Tests

```bash
./mvnw test
```

20 tests couvrent les repositories, services, mappers et l'intégration GraphQL.

---

## Structure du projet

```
src/
├── main/java/com/example/demo/
│   ├── controller/        # GraphQL controllers + exception handler
│   ├── service/           # Logique métier
│   ├── repository/        # Spring Data JPA
│   ├── mapper/            # DTO ↔ Entity converters
│   └── model/             # JPA entities
└── main/resources/
    ├── application.yaml         # Configuration principale
    ├── application-dev.yaml     # Profile dev
    ├── application-prod.yaml    # Profile prod
    ├── data-dev.sql             # Données seed (dev)
    └── graphql/schema.graphqls  # Schéma GraphQL
```

---

## Licence

Projet académique — PFE.
