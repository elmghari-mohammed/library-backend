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
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void shouldThrowWhenBorrowingUnavailableBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAvailable(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(RuntimeException.class, () -> loanService.borrow(1L, 1L));
    }

    @Test
    void shouldThrowWhenReturningAlreadyReturnedBook() {
        Book book = new Book();
        book.setId(1L);
        Member member = new Member();
        member.setId(1L);
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(LocalDate.now());

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(RuntimeException.class, () -> loanService.returnBook(1L));
    }
}
