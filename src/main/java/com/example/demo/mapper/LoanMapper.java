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
