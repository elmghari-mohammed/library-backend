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
