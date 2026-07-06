package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorInput(
    @NotBlank String name,
    String nationality,
    Integer birthYear
) {}
