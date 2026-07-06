package com.example.demo.repository;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Member;
import com.example.demo.model.Loan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
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
