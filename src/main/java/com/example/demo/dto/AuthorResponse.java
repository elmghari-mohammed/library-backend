package com.example.demo.dto;

import java.util.List;

public record AuthorResponse(Long id, String name, String nationality, Integer birthYear, List<Long> bookIds) {}
