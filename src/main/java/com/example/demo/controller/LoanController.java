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
