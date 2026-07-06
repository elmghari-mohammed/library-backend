# Journal de bord : j'apprends GraphQL en construisant une bibliothèque

> **Par où commencer avec GraphQL ?**  
> Moi non plus je ne savais pas. Alors j'ai fait ce que je fais toujours : j'ai codé.

Ce repo, c'est l'histoire de quelqu'un qui a décidé d'apprendre GraphQL pour de vrai. Pas en lisant, pas en regardant — en écrivant du code, jour après jour.

**Le code est ici :** [github.com/elmghari-mohammed/library-backend](https://github.com/elmghari-mohammed/library-backend)

---

## Jour 1 — Poser les bases

Premier réflexe : mettre en place le projet. Spring Boot 4.1.0, Java 21, Maven. Rien de sorcier.

Mais GraphQL, ça se passe comment avec Spring ? La réponse : `spring-boot-starter-graphql`. Une dépendance dans le `pom.xml`, et Spring s'occupe de tout :
- Le endpoint `POST /graphql` (que j'ai renommé en `/biblio` plus tard)
- Le parsing des requêtes
- L'appel aux bons résolveurs

Première leçon du jour : **avec spring-graphql, on écrit juste des `@QueryMapping` et `@MutationMapping` — Spring fait le reste.**

## Jour 2 — Les entités

Quatre tables. Quatre classes Java. La base de données d'une bibliothèque, quoi :

- **Author** — Victor Hugo, George Orwell, J.K. Rowling
- **Book** — Les Misérables, 1984, Harry Potter…
- **Member** — les gens qui empruntent
- **Loan** — qui a emprunté quoi et quand

J'ai découvert qu'avec Spring Data JPA, pas besoin d'écrire le SQL. Une interface, une méthode, et c'est bon.

Deuxième leçon : **les repositories Spring Data, c'est magique.** Tu déclares `findByCategory(String category)`, et Hibernate écrit la requête pour toi.

## Jour 3 — La logique métier

C'est là que ça devient intéressant. Un service par domaine.

Le plus fun : le `LoanService`. Emprunter un livre, c'est pas juste `INSERT INTO loans`. C'est :
1. Vérifier que le livre existe
2. Vérifier qu'il est disponible
3. Le marquer comme non disponible
4. Créer l'emprunt
5. Et si quelque chose plante, tout annuler (merci `@Transactional`)

Troisième leçon : **une mutation GraphLayout, c'est rarement une seule opération. C'est une mini-orchestration.**

## Jour 4 — Le GraphQL Schema

Fini le SQL, place à `schema.graphqls`. C'est le contrat entre mon API et ceux qui l'appellent :

```graphql
type Book {
  id: ID!
  title: String!
  isbn: String!
  category: String!
  authorId: ID!
  available: Boolean!
}

type Mutation {
  borrowBook(bookId: ID!, memberId: ID!): Loan!
  returnBook(loanId: ID!): Loan!
}
```

Quatrième leçon : **le schéma, c'est la documentation qui s'exécute.** Pas de décalage entre ce que tu promets et ce que tu livres.

## Jour 5 — Les DTOs et les mappers

J'ai appris à la dure pourquoi on n'expose pas les entités JPA directement. Les relations JPA (`@ManyToOne`, `@OneToMany`), c'est pratique, mais ça crée des problèmes :
- Sérialisation circulaire
- N+1 queries
- Trop d'infos qui fuient

Solution : **des DTOs (records Java) et des mappers statiques.** Une couche fine qui transforme Author → AuthorResponse.

Cinquième leçon : **DTO != code en trop. DTO = tranquillité d'esprit.**

## Jour 6 — Les tests

20 tests. Unitaires et intégration.

Le test le plus satisfaisant : emprunter deux fois le même livre. Première fois → OK. Deuxième fois → erreur. Voir le test passer, c'est voir que tout tient.

```java
graphQlTester.document("mutation { borrowBook(bookId: 1, memberId: 1) { id } }")
    .execute()
    .errors()
    .expect(e -> e.getMessage().contains("Book is not available"));
```

Sixième leçon : **GraphQL + Spring Boot + H2 = stack idéale pour apprendre. Tu testes vite, tu itères vite.**

## Jour 7 — Le polish

- **Profiles Spring** : `dev` (avec données seedées, H2 console, SQL visible) et `prod` (sobre et sécurisé)
- **Chemin personnalisé** : `/biblio` au lieu de `/graphql` — une ligne dans `application.yaml`
- **Variables d'environnement** : `.env` pour la config, `.env.example` pour les collègues
- **README** : ce que tu es en train de lire

---

## Pour lancer le projet

```bash
git clone https://github.com/elmghari-mohammed/library-backend.git
cd library-backend
./mvnw spring-boot:run
```

Puis ouvre un terminal et teste :

```bash
curl -X POST http://localhost:8080/biblio \
  -H "Content-Type: application/json" \
  -d '{"query":"{ allBooks { id title category } }"}'
```

Tu veux voir tous les auteurs seedés ? Va sur `http://localhost:8080/graphiql` et tape `{ allAuthors { id name nationality } }`.

---

## Ce que j'ai retenu

| Leçon | En résumé |
|-------|-----------|
| GraphQL ≠ magie | `@QueryMapping` appelle juste une méthode Java |
| DTOs obligatoires | Ne jamais exposer ses entités JPA |
| Transaction partout | `@Transactional` sur chaque service |
| Validation manuelle | spring-graphql ne gère pas `@Valid` automatiquement |
| Schéma = contrat | `schema.graphqls` est ta doc vivante |

---

## La suite ?

Le projet est jeune. J'aimerais :
- Connecter une vraie base (PostgreSQL)
- Ajouter un petit frontend (React + Apollo)
- Protéger l'API avec JWT
- Ajouter la pagination

Mais déjà, je suis content. J'ai appris GraphQL. En codant. Pas en regardant des vidéos.

---

*Si ce projet t'a aidé, n'hésite pas à laisser une ⭐ sur [GitHub](https://github.com/elmghari-mohammed/library-backend).*  
*Si t'as des questions, ouvre une issue — je serai content d'échanger.*
