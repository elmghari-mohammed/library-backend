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
