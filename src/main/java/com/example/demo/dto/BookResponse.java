package com.example.demo.dto;

public record BookResponse(Long id, String title, String isbn, String category, Long authorId, boolean available) {}
