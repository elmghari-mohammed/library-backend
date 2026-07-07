# Bibliothèque GraphQL

API GraphQL de gestion de bibliothèque construite avec **Java 21**, **Spring Boot 4.1.0** et **Spring Data JPA**.

Ce projet est un exercice pratique pour apprendre GraphQL avec l'écosystème Spring. Il couvre les queries, mutations, résolveurs, DTOs, mappers et la gestion d'erreurs — le tout sur une base H2 en mémoire.

**Code source :** [github.com/elmghari-mohammed/library-backend](https://github.com/elmghari-mohammed/library-backend)

---

## Domaines

- **Auteur** — nom, nationalité, année de naissance
- **Livre** — titre, ISBN, catégorie, disponibilité, lié à un auteur
- **Membre** — nom, email, téléphone
- **Emprunt** — livre emprunté, membre, dates

---

## Stack

Java 21 · Spring Boot 4.1.0 · spring-graphql · Spring Data JPA · H2 · Maven

---

## Lancer

```bash
git clone https://github.com/elmghari-mohammed/library-backend.git
cd library-backend
./mvnw spring-boot:run
```

- **API :** `POST http://localhost:8080/biblio`
- **GraphiQL :** `http://localhost:8080/graphiql` (dev)

```bash
curl -X POST http://localhost:8080/biblio \
  -H "Content-Type: application/json" \
  -d '{"query":"{ allBooks { id title category available } }"}'
```

---

## Profiles

| Profile | Usage |
|---------|-------|
| `dev` (défaut) | SQL visible, H2 console, données seedées (3 auteurs, 5 livres, 2 membres) |
| `prod` | SQL désactivé, GraphiQL désactivé, `ddl-auto: validate` |

---

## Tests

```bash
./mvnw test        # 20 tests
```

---

## Projet

```
src/main/java/com/example/demo/
├── controller/     # Résolveurs GraphQL + exception handler
├── service/        # Logique métier
├── repository/     # Spring Data JPA
├── mapper/         # DTO ↔ Entité
└── model/          # Entités JPA
src/main/resources/
├── application*.yaml
├── data-dev.sql
└── graphql/schema.graphqls
```
