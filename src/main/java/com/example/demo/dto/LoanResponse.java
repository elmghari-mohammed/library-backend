package com.example.demo.dto;

import java.time.LocalDate;

public record LoanResponse(Long id, Long bookId, Long memberId, LocalDate loanDate, LocalDate returnDate) {}
