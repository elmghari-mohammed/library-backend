package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookInput(
    @NotBlank String title,
    @NotBlank String isbn,
    @NotBlank String category,
    @NotNull Long authorId
) {}
