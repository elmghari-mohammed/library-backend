# Bibliothèque GraphQL — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a GraphQL API for a library management system (books, authors, members, loans)

**Architecture:** Single Spring Boot app with layered packages (model → repository → service → controller), DTOs + mappers for entity/API separation, Spring Data JPA + H2 for persistence

**Tech Stack:** Java 21, Spring Boot 4.1.0, spring-graphql, Spring Data JPA, H2, Maven

## Global Constraints

- Package root: `com.example.demo`
- Java 21, Spring Boot 4.1.0, Maven
- Clean code & Single Responsibility throughout
- No overengineering — keep it simple
- Respect existing elements (patterns, naming, package structure)
- TDD: write failing test, verify fail, implement, verify pass, commit
- Mini-tasks with validation gates (compile/test between each)

---
### Task 1: Add dependencies and configuration

**Files:**
- Modify: `pom.xml`
- Modify: `src/main/resources/application.yaml`
- Create: `src/main/resources/application-test.yaml`

**Interfaces:**
- Consumes: nothing
- Produces: working Spring Boot with GraphQL + JPA + H2

- [ ] **Step 1: Update pom.xml**

Add inside `<dependencies>`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-graphql</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Add inside `<dependency>` section (keep existing micrometer + webmvc + test deps).

Clean up: remove Graphite registry dependency (not needed).

- [ ] **Step 2: Update application.yaml**

```yaml
spring:
  application:
    name: demo
  graphql:
    graphiql:
      enabled: true
  datasource:
    url: jdbc:h2:mem:bibliotheque
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

- [ ] **Step 3: Create application-test.yaml**

```yaml
spring:
  jpa:
    show-sql: false
```

- [ ] **Step 4: Compile to verify**

Run: `./mvnw clean compile`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add pom.xml src/main/resources/application.yaml src/main/resources/application-test.yaml
git commit -m "chore: add graphql, jpa, h2 dependencies and config"
```

---
### Task 2: Create JPA entities

**Files:**
- Create: `src/main/java/com/example/demo/model/Author.java`
- Create: `src/main/java/com/example/demo/model/Book.java`
- Create: `src/main/java/com/example/demo/model/Member.java`
- Create: `src/main/java/com/example/demo/model/Loan.java`

**Interfaces:**
- Consumes: nothing (standalone entities)
- Produces: 4 JPA entities used by repositories

- [ ] **Step 1: Write test for entities**

Create `src/test/java/com/example/demo/model/ModelTest.java`:

```java
package com.example.demo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class ModelTest {

    @Test
    void authorShouldHaveRequiredFields() {
        Author author = new Author();
        author.setName("Victor Hugo");
        assertThat(author.getName()).isEqualTo("Victor Hugo");
    }

    @Test
    void bookShouldLinkToAuthor() {
        Author author = new Author();
        author.setId(1L);
        Book book = new Book();
        book.setTitle("Les Misérables");
        book.setAuthor(author);
        assertThat(book.getAuthor()).isEqualTo(author);
    }

    @Test
    void loanShouldTrackDates() {
        Loan loan = new Loan();
        loan.setLoanDate(LocalDate.now());
        assertThat(loan.getLoanDate()).isEqualTo(LocalDate.now());
        assertThat(loan.getReturnDate()).isNull();
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=ModelTest`
Expected: Compilation errors (classes don't exist yet)

- [ ] **Step 3: Create Author entity**

`src/main/java/com/example/demo/model/Author.java`:

```java
package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String nationality;
    private Integer birthYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public Author() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}
```

- [ ] **Step 4: Create Book entity**

`src/main/java/com/example/demo/model/Book.java`:

```java
package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(nullable = false)
    private boolean available = true;

    public Book() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
```

- [ ] **Step 5: Create Member entity**

`src/main/java/com/example/demo/model/Member.java`:

```java
package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @OneToMany(mappedBy = "member")
    private List<Loan> loans = new ArrayList<>();

    public Member() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<Loan> getLoans() { return loans; }
    public void setLoans(List<Loan> loans) { this.loans = loans; }
}
```

- [ ] **Step 6: Create Loan entity**

`src/main/java/com/example/demo/model/Loan.java`:

```java
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate loanDate;

    private LocalDate returnDate;

    public Loan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}
```

- [ ] **Step 7: Run test to verify it passes**

Run: `./mvnw test -Dtest=ModelTest`
Expected: BUILD SUCCESS

- [ ] **Step 8: Commit**

```bash
git add src/main/java/com/example/demo/model/ src/test/java/com/example/demo/model/
git commit -m "feat: add JPA entities (Author, Book, Member, Loan)"
```

---
### Task 3: Create repositories

**Files:**
- Create: `src/main/java/com/example/demo/repository/AuthorRepository.java`
- Create: `src/main/java/com/example/demo/repository/BookRepository.java`
- Create: `src/main/java/com/example/demo/repository/MemberRepository.java`
- Create: `src/main/java/com/example/demo/repository/LoanRepository.java`

**Interfaces:**
- Consumes: model classes from Task 2
- Produces: 4 Spring Data JPA repositories used by services

- [ ] **Step 1: Write test for repositories**

`src/test/java/com/example/demo/repository/RepositoryTest.java`:

```java
package com.example.demo.repository;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Member;
import com.example.demo.model.Loan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private LoanRepository loanRepository;

    @Test
    void shouldSaveAndFindAuthor() {
        Author author = new Author();
        author.setName("Victor Hugo");
        authorRepository.save(author);
        assertThat(authorRepository.findById(author.getId())).isPresent();
    }

    @Test
    void shouldFindBooksByAuthor() {
        Author author = new Author();
        author.setName("Victor Hugo");
        em.persist(author);

        Book book = new Book();
        book.setTitle("Les Misérables");
        book.setIsbn("978-0-00-000000-1");
        book.setCategory("Fiction");
        book.setAuthor(author);
        bookRepository.save(book);

        List<Book> books = bookRepository.findByAuthorId(author.getId());
        assertThat(books).hasSize(1);
    }

    @Test
    void shouldFindActiveLoans() {
        Author author = new Author();
        author.setName("Author");
        em.persist(author);

        Book book = new Book();
        book.setTitle("Book");
        book.setIsbn("978-0-00-000000-2");
        book.setCategory("Fiction");
        book.setAuthor(author);
        em.persist(book);

        Member member = new Member();
        member.setName("Member");
        member.setEmail("member@test.com");
        em.persist(member);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loanRepository.save(loan);

        assertThat(loanRepository.findByReturnDateIsNull()).hasSize(1);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=RepositoryTest`
Expected: Compilation errors

- [ ] **Step 3: Create repositories**

`src/main/java/com/example/demo/repository/AuthorRepository.java`:

```java
package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
```

`src/main/java/com/example/demo/repository/BookRepository.java`:

```java
package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByCategory(String category);
}
```

`src/main/java/com/example/demo/repository/MemberRepository.java`:

```java
package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {}
```

`src/main/java/com/example/demo/repository/LoanRepository.java`:

```java
package com.example.demo.repository;

import com.example.demo.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReturnDateIsNull();
    List<Loan> findByMemberId(Long memberId);
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=RepositoryTest`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/example/demo/repository/ src/test/java/com/example/demo/repository/
git commit -m "feat: add Spring Data JPA repositories"
```

---
### Task 4: Create DTOs

**Files:**
- Create: `src/main/java/com/example/demo/dto/AuthorInput.java`
- Create: `src/main/java/com/example/demo/dto/AuthorResponse.java`
- Create: `src/main/java/com/example/demo/dto/BookInput.java`
- Create: `src/main/java/com/example/demo/dto/BookResponse.java`
- Create: `src/main/java/com/example/demo/dto/MemberInput.java`
- Create: `src/main/java/com/example/demo/dto/MemberResponse.java`
- Create: `src/main/java/com/example/demo/dto/LoanResponse.java`

**Interfaces:**
- Consumes: model classes from Task 2
- Produces: 7 DTO classes (no LoanInput — borrow/return use simple params)

- [ ] **Step 1: Create AuthorInput**

```java
package com.example.demo.dto;

public record AuthorInput(String name, String nationality, Integer birthYear) {}
```

- [ ] **Step 2: Create AuthorResponse**

```java
package com.example.demo.dto;

import java.util.List;

public record AuthorResponse(Long id, String name, String nationality, Integer birthYear, List<Long> bookIds) {}
```

- [ ] **Step 3: Create BookInput**

```java
package com.example.demo.dto;

public record BookInput(String title, String isbn, String category, Long authorId) {}
```

- [ ] **Step 4: Create BookResponse**

```java
package com.example.demo.dto;

public record BookResponse(Long id, String title, String isbn, String category, Long authorId, boolean available) {}
```

- [ ] **Step 5: Create MemberInput**

```java
package com.example.demo.dto;

public record MemberInput(String name, String email, String phone) {}
```

- [ ] **Step 6: Create MemberResponse**

```java
package com.example.demo.dto;

public record MemberResponse(Long id, String name, String email, String phone) {}
```

- [ ] **Step 7: Create LoanResponse**

```java
package com.example.demo.dto;

import java.time.LocalDate;

public record LoanResponse(Long id, Long bookId, Long memberId, LocalDate loanDate, LocalDate returnDate) {}
```

- [ ] **Step 8: Compile to verify**

Run: `./mvnw clean compile`
Expected: BUILD SUCCESS

- [ ] **Step 9: Commit**

```bash
git add src/main/java/com/example/demo/dto/
git commit -m "feat: add DTOs (inputs and responses)"
```

---
### Task 5: Create mappers

**Files:**
- Create: `src/main/java/com/example/demo/mapper/AuthorMapper.java`
- Create: `src/main/java/com/example/demo/mapper/BookMapper.java`
- Create: `src/main/java/com/example/demo/mapper/MemberMapper.java`
- Create: `src/main/java/com/example/demo/mapper/LoanMapper.java`

**Interfaces:**
- Consumes: model classes + DTOs from Tasks 2, 4
- Produces: 4 mapper classes used by services

- [ ] **Step 1: Write test for mappers**

`src/test/java/com/example/demo/mapper/MapperTest.java`:

```java
package com.example.demo.mapper;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.dto.LoanResponse;
import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Member;
import com.example.demo.model.Loan;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    @Test
    void shouldMapAuthorInputToEntity() {
        AuthorInput input = new AuthorInput("Victor Hugo", "Française", 1802);
        Author author = AuthorMapper.toEntity(input);
        assertThat(author.getName()).isEqualTo("Victor Hugo");
        assertThat(author.getNationality()).isEqualTo("Française");
    }

    @Test
    void shouldMapAuthorEntityToResponse() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Victor Hugo");
        AuthorResponse response = AuthorMapper.toResponse(author);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Victor Hugo");
    }

    @Test
    void shouldMapBookInputToEntity() {
        BookInput input = new BookInput("Les Misérables", "978-0-00-000000-1", "Fiction", 1L);
        Book book = BookMapper.toEntity(input);
        assertThat(book.getTitle()).isEqualTo("Les Misérables");
    }

    @Test
    void shouldMapLoanEntityToResponse() {
        Book book = new Book();
        book.setId(1L);
        Member member = new Member();
        member.setId(1L);
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        LoanResponse response = LoanMapper.toResponse(loan);
        assertThat(response.bookId()).isEqualTo(1L);
        assertThat(response.memberId()).isEqualTo(1L);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=MapperTest`
Expected: Compilation errors

- [ ] **Step 3: Create AuthorMapper**

```java
package com.example.demo.mapper;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.model.Author;
import java.util.Collections;
import java.util.Optional;

public class AuthorMapper {

    public static Author toEntity(AuthorInput input) {
        Author author = new Author();
        author.setName(input.name());
        author.setNationality(input.nationality());
        author.setBirthYear(input.birthYear());
        return author;
    }

    public static void updateEntity(Author author, AuthorInput input) {
        author.setName(input.name());
        author.setNationality(input.nationality());
        author.setBirthYear(input.birthYear());
    }

    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
            author.getId(),
            author.getName(),
            author.getNationality(),
            author.getBirthYear(),
            Optional.ofNullable(author.getBooks())
                .map(books -> books.stream().map(Book::getId).toList())
                .orElse(Collections.emptyList())
        );
    }
}
```

- [ ] **Step 4: Create BookMapper**

```java
package com.example.demo.mapper;

import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.model.Author;
import com.example.demo.model.Book;

public class BookMapper {

    public static Book toEntity(BookInput input) {
        Book book = new Book();
        book.setTitle(input.title());
        book.setIsbn(input.isbn());
        book.setCategory(input.category());
        Author author = new Author();
        author.setId(input.authorId());
        book.setAuthor(author);
        return book;
    }

    public static void updateEntity(Book book, BookInput input) {
        book.setTitle(input.title());
        book.setIsbn(input.isbn());
        book.setCategory(input.category());
        Author author = new Author();
        author.setId(input.authorId());
        book.setAuthor(author);
    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getIsbn(),
            book.getCategory(),
            book.getAuthor().getId(),
            book.isAvailable()
        );
    }
}
```

- [ ] **Step 5: Create MemberMapper**

```java
package com.example.demo.mapper;

import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.model.Member;

public class MemberMapper {

    public static Member toEntity(MemberInput input) {
        Member member = new Member();
        member.setName(input.name());
        member.setEmail(input.email());
        member.setPhone(input.phone());
        return member;
    }

    public static void updateEntity(Member member, MemberInput input) {
        member.setName(input.name());
        member.setEmail(input.email());
        member.setPhone(input.phone());
    }

    public static MemberResponse toResponse(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getPhone()
        );
    }
}
```

- [ ] **Step 6: Create LoanMapper**

```java
package com.example.demo.mapper;

import com.example.demo.dto.LoanResponse;
import com.example.demo.model.Loan;

public class LoanMapper {

    public static LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
            loan.getId(),
            loan.getBook().getId(),
            loan.getMember().getId(),
            loan.getLoanDate(),
            loan.getReturnDate()
        );
    }
}
```

- [ ] **Step 7: Run test to verify it passes**

Run: `./mvnw test -Dtest=MapperTest`
Expected: BUILD SUCCESS

- [ ] **Step 8: Commit**

```bash
git add src/main/java/com/example/demo/mapper/ src/test/java/com/example/demo/mapper/
git commit -m "feat: add entity-DTO mappers"
```

---
### Task 6: Create services

**Files:**
- Create: `src/main/java/com/example/demo/service/AuthorService.java`
- Create: `src/main/java/com/example/demo/service/BookService.java`
- Create: `src/main/java/com/example/demo/service/MemberService.java`
- Create: `src/main/java/com/example/demo/service/LoanService.java`

**Interfaces:**
- Consumes: repositories (Task 3), mappers (Task 5)
- Produces: 4 service classes used by controllers

- [ ] **Step 1: Write test for AuthorService**

`src/test/java/com/example/demo/service/AuthorServiceTest.java`:

```java
package com.example.demo.service;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock private AuthorRepository authorRepository;
    @InjectMocks private AuthorService authorService;

    @Test
    void shouldReturnAllAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(new Author()));
        List<AuthorResponse> result = authorService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindAuthorById() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Victor Hugo");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        AuthorResponse result = authorService.findById(1L);
        assertThat(result.name()).isEqualTo("Victor Hugo");
    }

    @Test
    void shouldCreateAuthor() {
        AuthorInput input = new AuthorInput("Victor Hugo", "Française", 1802);
        Author saved = new Author();
        saved.setId(1L);
        saved.setName("Victor Hugo");
        when(authorRepository.save(any())).thenReturn(saved);

        AuthorResponse result = authorService.create(input);
        assertThat(result.id()).isEqualTo(1L);
    }
}
```

- [ ] **Step 2: Write test for LoanService**

`src/test/java/com/example/demo/service/LoanServiceTest.java`:

```java
package com.example.demo.service;

import com.example.demo.dto.LoanResponse;
import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.model.Member;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock private LoanRepository loanRepository;
    @Mock private BookRepository bookRepository;
    @Mock private MemberRepository memberRepository;
    @InjectMocks private LoanService loanService;

    @Test
    void shouldBorrowBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        Member member = new Member();
        member.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        LoanResponse result = loanService.borrow(1L, 1L);
        assertThat(result.bookId()).isEqualTo(1L);
        assertThat(result.returnDate()).isNull();
    }
}
```

- [ ] **Step 3: Run tests to verify they fail**

Run: `./mvnw test -Dtest=AuthorServiceTest,LoanServiceTest`
Expected: Compilation errors

- [ ] **Step 4: Create AuthorService**

```java
package com.example.demo.service;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorResponse> findAll() {
        return authorRepository.findAll().stream()
            .map(AuthorMapper::toResponse)
            .toList();
    }

    public AuthorResponse findById(Long id) {
        return authorRepository.findById(id)
            .map(AuthorMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Author not found: " + id));
    }

    public AuthorResponse create(AuthorInput input) {
        Author author = AuthorMapper.toEntity(input);
        return AuthorMapper.toResponse(authorRepository.save(author));
    }

    public AuthorResponse update(Long id, AuthorInput input) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Author not found: " + id));
        AuthorMapper.updateEntity(author, input);
        return AuthorMapper.toResponse(authorRepository.save(author));
    }

    public boolean delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found: " + id);
        }
        authorRepository.deleteById(id);
        return true;
    }
}
```

- [ ] **Step 5: Create BookService**

```java
package com.example.demo.service;

import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookResponse> findAll(String category) {
        List<Book> books = (category != null && !category.isBlank())
            ? bookRepository.findByCategory(category)
            : bookRepository.findAll();
        return books.stream().map(BookMapper::toResponse).toList();
    }

    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
            .map(BookMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public List<BookResponse> findByAuthorId(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
            .map(BookMapper::toResponse)
            .toList();
    }

    public BookResponse create(BookInput input) {
        if (!authorRepository.existsById(input.authorId())) {
            throw new RuntimeException("Author not found: " + input.authorId());
        }
        Book book = BookMapper.toEntity(input);
        return BookMapper.toResponse(bookRepository.save(book));
    }

    public BookResponse update(Long id, BookInput input) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        if (!authorRepository.existsById(input.authorId())) {
            throw new RuntimeException("Author not found: " + input.authorId());
        }
        BookMapper.updateEntity(book, input);
        return BookMapper.toResponse(bookRepository.save(book));
    }

    public boolean delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
        return true;
    }
}
```

- [ ] **Step 6: Create MemberService**

```java
package com.example.demo.service;

import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
            .map(MemberMapper::toResponse)
            .toList();
    }

    public MemberResponse findById(Long id) {
        return memberRepository.findById(id)
            .map(MemberMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Member not found: " + id));
    }

    public MemberResponse create(MemberInput input) {
        Member member = MemberMapper.toEntity(input);
        return MemberMapper.toResponse(memberRepository.save(member));
    }

    public MemberResponse update(Long id, MemberInput input) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        MemberMapper.updateEntity(member, input);
        return MemberMapper.toResponse(memberRepository.save(member));
    }

    public boolean delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Member not found: " + id);
        }
        memberRepository.deleteById(id);
        return true;
    }
}
```

- [ ] **Step 7: Create LoanService**

```java
package com.example.demo.service;

import com.example.demo.dto.LoanResponse;
import com.example.demo.mapper.LoanMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.model.Member;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public LoanResponse borrow(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));
        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available: " + bookId);
        }
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

        book.setAvailable(false);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());

        return LoanMapper.toResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));
        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Book already returned");
        }

        loan.setReturnDate(LocalDate.now());
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return LoanMapper.toResponse(loanRepository.save(loan));
    }

    public List<LoanResponse> findAll() {
        return loanRepository.findAll().stream()
            .map(LoanMapper::toResponse)
            .toList();
    }

    public List<LoanResponse> findActive() {
        return loanRepository.findByReturnDateIsNull().stream()
            .map(LoanMapper::toResponse)
            .toList();
    }

    public List<LoanResponse> findByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId).stream()
            .map(LoanMapper::toResponse)
            .toList();
    }
}
```

- [ ] **Step 8: Run tests to verify they pass**

Run: `./mvnw test -Dtest=AuthorServiceTest,LoanServiceTest`
Expected: BUILD SUCCESS

- [ ] **Step 9: Commit**

```bash
git add src/main/java/com/example/demo/service/ src/test/java/com/example/demo/service/
git commit -m "feat: add business logic services"
```

---
### Task 7: Create GraphQL schema and controllers

**Files:**
- Create: `src/main/resources/graphql/schema.graphqls`
- Create: `src/main/java/com/example/demo/controller/AuthorController.java`
- Create: `src/main/java/com/example/demo/controller/BookController.java`
- Create: `src/main/java/com/example/demo/controller/MemberController.java`
- Create: `src/main/java/com/example/demo/controller/LoanController.java`

**Interfaces:**
- Consumes: services (Task 6)
- Produces: working GraphQL API

- [ ] **Step 1: Create GraphQL schema**

`src/main/resources/graphql/schema.graphqls`:

```graphql
type Author {
    id: ID!
    name: String!
    nationality: String
    birthYear: Int
    bookIds: [ID!]!
}

type Book {
    id: ID!
    title: String!
    isbn: String!
    category: String!
    authorId: ID!
    available: Boolean!
}

type Member {
    id: ID!
    name: String!
    email: String!
    phone: String
}

type Loan {
    id: ID!
    bookId: ID!
    memberId: ID!
    loanDate: String!
    returnDate: String
}

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

type Query {
    allAuthors: [Author!]!
    authorById(id: ID!): Author
    allBooks(category: String): [Book!]!
    bookById(id: ID!): Book
    booksByAuthor(authorId: ID!): [Book!]!
    allMembers: [Member!]!
    memberById(id: ID!): Member
    allLoans: [Loan!]!
    activeLoans: [Loan!]!
    loansByMember(memberId: ID!): [Loan!]!
}

type Mutation {
    createAuthor(input: AuthorInput!): Author!
    updateAuthor(id: ID!, input: AuthorInput!): Author!
    deleteAuthor(id: ID!): Boolean!
    createBook(input: BookInput!): Book!
    updateBook(id: ID!, input: BookInput!): Book!
    deleteBook(id: ID!): Boolean!
    createMember(input: MemberInput!): Member!
    updateMember(id: ID!, input: MemberInput!): Member!
    deleteMember(id: ID!): Boolean!
    borrowBook(bookId: ID!, memberId: ID!): Loan!
    returnBook(loanId: ID!): Loan!
}
```

- [ ] **Step 2: Write integration test**

`src/test/java/com/example/demo/controller/GraphQLIntegrationTest.java`:

```java
package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest
@AutoConfigureGraphQlTester
class GraphQLIntegrationTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldCreateAndQueryAuthor() {
        String mutation = """
            mutation {
                createAuthor(input: { name: "Victor Hugo", nationality: "Française", birthYear: 1802 }) {
                    id
                    name
                    nationality
                }
            }
        """;

        graphQlTester.document(mutation)
            .execute()
            .path("createAuthor.name")
            .entity(String.class)
            .isEqualTo("Victor Hugo");
    }

    @Test
    void shouldCreateAndQueryBook() {
        String createAuthor = """
            mutation { createAuthor(input: { name: "Author" }) { id } }
        """;
        var authorResult = graphQlTester.document(createAuthor).execute();
        Long authorId = authorResult.path("createAuthor.id").entity(Long.class).get();

        String createBook = String.format("""
            mutation {
                createBook(input: { title: "Test Book", isbn: "978-0-00-000000-3", category: "Fiction", authorId: %d }) {
                    id
                    title
                }
            }
        """, authorId);

        graphQlTester.document(createBook)
            .execute()
            .path("createBook.title")
            .entity(String.class)
            .isEqualTo("Test Book");
    }
}
```

- [ ] **Step 3: Run test to verify it fails**

Run: `./mvnw test -Dtest=GraphQLIntegrationTest`
Expected: Compilation errors

- [ ] **Step 4: Create AuthorController**

```java
package com.example.demo.controller;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.service.AuthorService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @QueryMapping
    public List<AuthorResponse> allAuthors() {
        return authorService.findAll();
    }

    @QueryMapping
    public AuthorResponse authorById(@Argument Long id) {
        return authorService.findById(id);
    }

    @MutationMapping
    public AuthorResponse createAuthor(@Argument AuthorInput input) {
        return authorService.create(input);
    }

    @MutationMapping
    public AuthorResponse updateAuthor(@Argument Long id, @Argument AuthorInput input) {
        return authorService.update(id, input);
    }

    @MutationMapping
    public boolean deleteAuthor(@Argument Long id) {
        return authorService.delete(id);
    }
}
```

- [ ] **Step 5: Create BookController**

```java
package com.example.demo.controller;

import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<BookResponse> allBooks(@Argument String category) {
        return bookService.findAll(category);
    }

    @QueryMapping
    public BookResponse bookById(@Argument Long id) {
        return bookService.findById(id);
    }

    @QueryMapping
    public List<BookResponse> booksByAuthor(@Argument Long authorId) {
        return bookService.findByAuthorId(authorId);
    }

    @MutationMapping
    public BookResponse createBook(@Argument BookInput input) {
        return bookService.create(input);
    }

    @MutationMapping
    public BookResponse updateBook(@Argument Long id, @Argument BookInput input) {
        return bookService.update(id, input);
    }

    @MutationMapping
    public boolean deleteBook(@Argument Long id) {
        return bookService.delete(id);
    }
}
```

- [ ] **Step 6: Create MemberController**

```java
package com.example.demo.controller;

import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.MemberService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @QueryMapping
    public List<MemberResponse> allMembers() {
        return memberService.findAll();
    }

    @QueryMapping
    public MemberResponse memberById(@Argument Long id) {
        return memberService.findById(id);
    }

    @MutationMapping
    public MemberResponse createMember(@Argument MemberInput input) {
        return memberService.create(input);
    }

    @MutationMapping
    public MemberResponse updateMember(@Argument Long id, @Argument MemberInput input) {
        return memberService.update(id, input);
    }

    @MutationMapping
    public boolean deleteMember(@Argument Long id) {
        return memberService.delete(id);
    }
}
```

- [ ] **Step 7: Create LoanController**

```java
package com.example.demo.controller;

import com.example.demo.dto.LoanResponse;
import com.example.demo.service.LoanService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @QueryMapping
    public List<LoanResponse> allLoans() {
        return loanService.findAll();
    }

    @QueryMapping
    public List<LoanResponse> activeLoans() {
        return loanService.findActive();
    }

    @QueryMapping
    public List<LoanResponse> loansByMember(@Argument Long memberId) {
        return loanService.findByMemberId(memberId);
    }

    @MutationMapping
    public LoanResponse borrowBook(@Argument Long bookId, @Argument Long memberId) {
        return loanService.borrow(bookId, memberId);
    }

    @MutationMapping
    public LoanResponse returnBook(@Argument Long loanId) {
        return loanService.returnBook(loanId);
    }
}
```

- [ ] **Step 8: Run test to verify it passes**

Run: `./mvnw test -Dtest=GraphQLIntegrationTest`
Expected: BUILD SUCCESS

- [ ] **Step 9: Commit**

```bash
git add src/main/resources/graphql/ src/main/java/com/example/demo/controller/ src/test/java/com/example/demo/controller/
git commit -m "feat: add GraphQL schema and controllers"
```

---
### Task 8: Add global error handling

**Files:**
- Create: `src/main/java/com/example/demo/controller/GraphQLExceptionHandler.java`

**Interfaces:**
- Consumes: nothing
- Produces: centralized error handling for GraphQL

- [ ] **Step 1: Write test**

Add to `GraphQLIntegrationTest.java`:

```java
@Test
void shouldReturnErrorForNonExistentAuthor() {
    graphQlTester.document("""
        query { authorById(id: 999) { name } }
    """).execute()
    .errors()
    .expect(e -> e.getMessage().contains("Author not found"));
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=GraphQLIntegrationTest`
Expected: the test may pass already if Spring Boot handles it, but we can add a custom handler

- [ ] **Step 3: Create GraphQLExceptionHandler**

```java
package com.example.demo.controller;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GraphQLExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handle(RuntimeException ex) {
        return GraphqlErrorBuilder.newError()
            .message(ex.getMessage())
            .errorType(ErrorType.BAD_REQUEST)
            .build();
    }
}
```

- [ ] **Step 4: Run tests to verify**

Run: `./mvnw test`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/example/demo/controller/GraphQLExceptionHandler.java src/test/java/com/example/demo/controller/GraphQLIntegrationTest.java
git commit -m "feat: add GraphQL error handling"
```

---
### Task 9: Final verification

- [ ] **Step 1: Run full test suite**

Run: `./mvnw clean test`
Expected: BUILD SUCCESS (all tests pass)

- [ ] **Step 2: Verify the app starts**

Run: `./mvnw spring-boot:run` in background, wait 10 seconds, then:

```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ allAuthors { id name } }"}'
```

Expected: `{"data":{"allAuthors":[]}}`

Stop the server after verification.

- [ ] **Step 3: Commit any final changes**

```bash
git add -A
git commit -m "chore: final adjustments after verification"
```

---
## Self-Review Checklist

- [ ] Every spec requirement maps to at least one task
- [ ] No TBD, TODO, or placeholder patterns in the plan
- [ ] Type consistency: method signatures and field names match across tasks
- [ ] pom.xml updates include all required dependencies (graphql, jpa, h2)
- [ ] GraphQL schema matches controller method names and argument types
- [ ] Mapper method names consistent across all 4 mappers
- [ ] Service method names match controller expectations
