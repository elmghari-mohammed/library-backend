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
