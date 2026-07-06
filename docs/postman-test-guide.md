# Guide de test GraphQL avec Postman

## Configuration

**URL :** `POST http://localhost:8080/graphql`
**Headers :**
```
Content-Type: application/json
```

---

## 1. Auteur (Author)

### Créer un auteur

**Request :**
```json
{
  "query": "mutation { createAuthor(input: { name: \"Victor Hugo\", nationality: \"Française\", birthYear: 1802 }) { id name nationality birthYear } }"
}
```

**Response :**
```json
{
  "data": {
    "createAuthor": {
      "id": "1",
      "name": "Victor Hugo",
      "nationality": "Française",
      "birthYear": 1802
    }
  }
}
```

### Lister tous les auteurs

**Request :**
```json
{
  "query": "{ allAuthors { id name nationality birthYear } }"
}
```

**Response :**
```json
{
  "data": {
    "allAuthors": [
      {
        "id": "1",
        "name": "Victor Hugo",
        "nationality": "Française",
        "birthYear": 1802
      }
    ]
  }
}
```

### Chercher un auteur par ID

**Request :**
```json
{
  "query": "{ authorById(id: 1) { id name nationality bookIds } }"
}
```

**Response :**
```json
{
  "data": {
    "authorById": {
      "id": "1",
      "name": "Victor Hugo",
      "nationality": "Française",
      "bookIds": []
    }
  }
}
```

### Modifier un auteur

**Request :**
```json
{
  "query": "mutation { updateAuthor(id: 1, input: { name: \"Victor-Marie Hugo\", nationality: \"Française\", birthYear: 1802 }) { id name } }"
}
```

**Response :**
```json
{
  "data": {
    "updateAuthor": {
      "id": "1",
      "name": "Victor-Marie Hugo"
    }
  }
}
```

### Supprimer un auteur

**Request :**
```json
{
  "query": "mutation { deleteAuthor(id: 1) }"
}
```

**Response :**
```json
{
  "data": {
    "deleteAuthor": true
  }
}
```

---

## 2. Livre (Book)

### Créer un livre

**Request :**
```json
{
  "query": "mutation { createBook(input: { title: \"Les Misérables\", isbn: \"978-0-00-000000-1\", category: \"Fiction\", authorId: 1 }) { id title isbn category authorId available } }"
}
```

**Response :**
```json
{
  "data": {
    "createBook": {
      "id": "1",
      "title": "Les Misérables",
      "isbn": "978-0-00-000000-1",
      "category": "Fiction",
      "authorId": "1",
      "available": true
    }
  }
}
```

### Lister tous les livres

**Request :**
```json
{
  "query": "{ allBooks { id title isbn category authorId available } }"
}
```

**Response :**
```json
{
  "data": {
    "allBooks": [
      {
        "id": "1",
        "title": "Les Misérables",
        "isbn": "978-0-00-000000-1",
        "category": "Fiction",
        "authorId": "1",
        "available": true
      }
    ]
  }
}
```

### Filtrer les livres par catégorie

**Request :**
```json
{
  "query": "{ allBooks(category: \"Fiction\") { id title category } }"
}
```

**Response :**
```json
{
  "data": {
    "allBooks": [
      {
        "id": "1",
        "title": "Les Misérables",
        "category": "Fiction"
      }
    ]
  }
}
```

### Chercher les livres d'un auteur

**Request :**
```json
{
  "query": "{ booksByAuthor(authorId: 1) { id title } }"
}
```

**Response :**
```json
{
  "data": {
    "booksByAuthor": [
      {
        "id": "1",
        "title": "Les Misérables"
      }
    ]
  }
}
```

### Chercher un livre par ID

**Request :**
```json
{
  "query": "{ bookById(id: 1) { id title isbn category authorId available } }"
}
```

**Response :**
```json
{
  "data": {
    "bookById": {
      "id": "1",
      "title": "Les Misérables",
      "isbn": "978-0-00-000000-1",
      "category": "Fiction",
      "authorId": "1",
      "available": true
    }
  }
}
```

### Modifier un livre

**Request :**
```json
{
  "query": "mutation { updateBook(id: 1, input: { title: \"Les Misérables (Édition spéciale)\", isbn: \"978-0-00-000000-1\", category: \"Fiction\", authorId: 1 }) { id title } }"
}
```

**Response :**
```json
{
  "data": {
    "updateBook": {
      "id": "1",
      "title": "Les Misérables (Édition spéciale)"
    }
  }
}
```

### Supprimer un livre

**Request :**
```json
{
  "query": "mutation { deleteBook(id: 1) }"
}
```

**Response :**
```json
{
  "data": {
    "deleteBook": true
  }
}
```

---

## 3. Membre (Member)

### Créer un membre

**Request :**
```json
{
  "query": "mutation { createMember(input: { name: \"Jean Dupont\", email: \"jean.dupont@email.com\", phone: \"0123456789\" }) { id name email phone } }"
}
```

**Response :**
```json
{
  "data": {
    "createMember": {
      "id": "1",
      "name": "Jean Dupont",
      "email": "jean.dupont@email.com",
      "phone": "0123456789"
    }
  }
}
```

### Lister tous les membres

**Request :**
```json
{
  "query": "{ allMembers { id name email phone } }"
}
```

**Response :**
```json
{
  "data": {
    "allMembers": [
      {
        "id": "1",
        "name": "Jean Dupont",
        "email": "jean.dupont@email.com",
        "phone": "0123456789"
      }
    ]
  }
}
```

### Chercher un membre par ID

**Request :**
```json
{
  "query": "{ memberById(id: 1) { id name email } }"
}
```

**Response :**
```json
{
  "data": {
    "memberById": {
      "id": "1",
      "name": "Jean Dupont",
      "email": "jean.dupont@email.com"
    }
  }
}
```

### Modifier un membre

**Request :**
```json
{
  "query": "mutation { updateMember(id: 1, input: { name: \"Jean Dupont Jr\", email: \"jean.jr@email.com\", phone: \"0987654321\" }) { id name email } }"
}
```

**Response :**
```json
{
  "data": {
    "updateMember": {
      "id": "1",
      "name": "Jean Dupont Jr",
      "email": "jean.jr@email.com"
    }
  }
}
```

### Supprimer un membre

**Request :**
```json
{
  "query": "mutation { deleteMember(id: 1) }"
}
```

**Response :**
```json
{
  "data": {
    "deleteMember": true
  }
}
```

---

## 4. Emprunt (Loan)

### Emprunter un livre

**Request :**
```json
{
  "query": "mutation { borrowBook(bookId: 1, memberId: 1) { id bookId memberId loanDate returnDate } }"
}
```

**Response :**
```json
{
  "data": {
    "borrowBook": {
      "id": "1",
      "bookId": "1",
      "memberId": "1",
      "loanDate": "2026-07-06",
      "returnDate": null
    }
  }
}
```

### Retourner un livre

**Request :**
```json
{
  "query": "mutation { returnBook(loanId: 1) { id bookId memberId loanDate returnDate } }"
}
```

**Response :**
```json
{
  "data": {
    "returnBook": {
      "id": "1",
      "bookId": "1",
      "memberId": "1",
      "loanDate": "2026-07-06",
      "returnDate": "2026-07-06"
    }
  }
}
```

### Lister tous les emprunts

**Request :**
```json
{
  "query": "{ allLoans { id bookId memberId loanDate returnDate } }"
}
```

**Response :**
```json
{
  "data": {
    "allLoans": [
      {
        "id": "1",
        "bookId": "1",
        "memberId": "1",
        "loanDate": "2026-07-06",
        "returnDate": "2026-07-06"
      }
    ]
  }
}
```

### Emprunts en cours (non retournés)

**Request :**
```json
{
  "query": "{ activeLoans { id bookId memberId loanDate } }"
}
```

**Response :**
```json
{
  "data": {
    "activeLoans": []
  }
}
```

### Historique des emprunts d'un membre

**Request :**
```json
{
  "query": "{ loansByMember(memberId: 1) { id bookId loanDate returnDate } }"
}
```

**Response :**
```json
{
  "data": {
    "loansByMember": [
      {
        "id": "1",
        "bookId": "1",
        "loanDate": "2026-07-06",
        "returnDate": "2026-07-06"
      }
    ]
  }
}
```

---

## 5. Scénario complet (workflow typique)

Exécuter dans l'ordre :

### Étape 1 : Créer un auteur
```json
{ "query": "mutation { createAuthor(input: { name: \"George Orwell\", nationality: \"Britannique\", birthYear: 1903 }) { id name } }" }
```

### Étape 2 : Créer un livre
```json
{ "query": "mutation { createBook(input: { title: \"1984\", isbn: \"978-0-00-000000-2\", category: \"Dystopie\", authorId: 1 }) { id title } }" }
```

### Étape 3 : Créer un membre
```json
{ "query": "mutation { createMember(input: { name: \"Alice\", email: \"alice@email.com\" }) { id name } }" }
```

### Étape 4 : Emprunter le livre
```json
{ "query": "mutation { borrowBook(bookId: 1, memberId: 1) { id loanDate returnDate } }" }
```

### Étape 5 : Vérifier que le livre n'est plus disponible
```json
{ "query": "{ bookById(id: 1) { title available } }" }
```
→ `"available": false`

### Étape 6 : Retourner le livre
```json
{ "query": "mutation { returnBook(loanId: 1) { returnDate } }" }
```

### Étape 7 : Vérifier la disponibilité
```json
{ "query": "{ bookById(id: 1) { title available } }" }
```
→ `"available": true`

---

## 6. Gestion des erreurs

### Livre déjà emprunté
```json
{ "query": "mutation { borrowBook(bookId: 1, memberId: 1) { id } }" }
```
Effectuer deux fois → la deuxième fois retourne :
```json
{
  "errors": [
    {
      "message": "Book is not available: 1",
      "errorType": "BAD_REQUEST"
    }
  ]
}
```

### Entité inexistante
```json
{ "query": "{ authorById(id: 999) { name } }" }
```
**Response :**
```json
{
  "errors": [
    {
      "message": "Author not found: 999",
      "errorType": "BAD_REQUEST"
    }
  ]
}
```

### Validation (champ requis vide)
```json
{ "query": "mutation { createMember(input: { name: \"\", email: \"bad\" }) { id } }" }
```
**Response :**
```json
{
  "errors": [
    {
      "message": "Validation failed",
      "errorType": "BAD_REQUEST"
    }
  ]
}
```
