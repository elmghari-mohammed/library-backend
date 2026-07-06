# Bibliothèque GraphQL — Design Document

## Stack

- Java 21 + Spring Boot 4.1.0 + Maven
- spring-graphql + Spring Data JPA + H2
- Micrometer + Graphite registry (already present)

## Architecture

### Package structure

```
com.example.demo
├── model/
│   ├── Book.java
│   ├── Author.java
│   ├── Member.java
│   └── Loan.java
├── repository/
│   ├── BookRepository.java
│   ├── AuthorRepository.java
│   ├── MemberRepository.java
│   └── LoanRepository.java
├── service/
│   ├── BookService.java
│   ├── AuthorService.java
│   ├── MemberService.java
│   └── LoanService.java
├── controller/
│   ├── BookController.java
│   ├── AuthorController.java
│   ├── MemberController.java
│   └── LoanController.java
├── dto/
│   ├── BookInput.java
│   ├── BookResponse.java
│   ├── AuthorInput.java
│   ├── AuthorResponse.java
│   ├── MemberInput.java
│   ├── MemberResponse.java
│   ├── LoanInput.java
│   └── LoanResponse.java
├── mapper/
│   ├── BookMapper.java
│   ├── AuthorMapper.java
│   ├── MemberMapper.java
│   └── LoanMapper.java
└── DemoApplication.java
```

### Single Responsibility

| Package | Responsibility |
|---------|---------------|
| `model/` | JPA entities only |
| `repository/` | Spring Data JPA interfaces |
| `service/` | Business logic, validation |
| `controller/` | `@Controller` with `@QueryMapping` / `@MutationMapping` |
| `dto/` | Request inputs and response outputs (decoupled from entities) |
| `mapper/` | Conversion between Entity ↔ DTO |

Each service handles ONE domain (BookService → books only, LoanService → loans only, etc.).

## Data Model

### Author
- id: Long (auto-generated)
- name: String (required)
- nationality: String (optional)
- birthYear: Int (optional)

### Book
- id: Long (auto-generated)
- title: String (required)
- isbn: String (unique, required)
- category: String (required)
- author: ManyToOne → Author
- available: boolean (default true)

### Member
- id: Long (auto-generated)
- name: String (required)
- email: String (unique, required)
- phone: String (optional)

### Loan
- id: Long (auto-generated)
- book: ManyToOne → Book
- member: ManyToOne → Member
- loanDate: LocalDate (required)
- returnDate: LocalDate (optional, null = not returned yet)

### Relations
- Author 1:N Book (one author → many books)
- Book 1:N Loan (a book can be loaned multiple times over time)
- Member 1:N Loan (one member → many loans over time)
- `available` flag on Book controls whether it can be borrowed

## GraphQL Schema

### Types

```graphql
type Author {
  id: ID!
  name: String!
  nationality: String
  birthYear: Int
  books: [Book!]!
}

type Book {
  id: ID!
  title: String!
  isbn: String!
  category: String!
  author: Author!
  available: Boolean!
}

type Member {
  id: ID!
  name: String!
  email: String!
  phone: String
  loans: [Loan!]!
}

type Loan {
  id: ID!
  book: Book!
  member: Member!
  loanDate: String!
  returnDate: String
}
```

### Input Types

```graphql
input AuthorInput {
  name: String!
  nationality: String
  birthYear: Int
}

input BookInput {
  title: String!
  isbn: String!
  category: String!
  authorId: ID!
}

input MemberInput {
  name: String!
  email: String!
  phone: String
}
```

### Queries & Mutations

```graphql
type Query {
  # Authors
  allAuthors: [Author!]!
  authorById(id: ID!): Author

  # Books
  allBooks(category: String): [Book!]!
  bookById(id: ID!): Book
  booksByAuthor(authorId: ID!): [Book!]!

  # Members
  allMembers: [Member!]!
  memberById(id: ID!): Member

  # Loans
  allLoans: [Loan!]!
  activeLoans: [Loan!]!
  loansByMember(memberId: ID!): [Loan!]!
}

type Mutation {
  # Authors
  createAuthor(input: AuthorInput!): Author!
  updateAuthor(id: ID!, input: AuthorInput!): Author!
  deleteAuthor(id: ID!): Boolean!

  # Books
  createBook(input: BookInput!): Book!
  updateBook(id: ID!, input: BookInput!): Book!
  deleteBook(id: ID!): Boolean!

  # Members
  createMember(input: MemberInput!): Member!
  updateMember(id: ID!, input: MemberInput!): Member!
  deleteMember(id: ID!): Boolean!

  # Loans
  borrowBook(bookId: ID!, memberId: ID!): Loan!
  returnBook(loanId: ID!): Loan!
}
```

### GraphQL Good Practices

- Input types for all mutations (not raw scalars)
- camelCase field names
- Nullable fields where appropriate (returnDate, phone, nationality)
- Business errors thrown as `RuntimeException` with `@GraphQlExceptionHandler`
- Validation via `jakarta.validation` on Input DTOs

## Development Commands

```bash
./mvnw spring-boot:run                # start on port 8080
./mvnw test                           # run all tests
./mvnw clean compile                  # build without tests
```

GraphQL endpoint: `POST /graphql`
GraphiQL IDE: `GET /graphiql` (if spring-graphiql on classpath)

## Constraints

- No image handling
- Clean code & Single Responsibility throughout
- No overengineering — keep it simple
- Respect existing elements (patterns, naming, package structure)
- Mini-tasks with validation gates (compile/test between steps)
