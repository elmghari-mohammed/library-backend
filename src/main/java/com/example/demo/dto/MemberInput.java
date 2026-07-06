package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberInput(
    @NotBlank String name,
    @NotBlank @Email String email,
    String phone
) {}
