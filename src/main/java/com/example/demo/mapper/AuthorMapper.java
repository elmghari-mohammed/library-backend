package com.example.demo.mapper;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.model.Author;
import com.example.demo.model.Book;
import java.util.Collections;
import java.util.Optional;

public class AuthorMapper {

    public static Author toEntity(AuthorInput input) {
        Author author = new Author();
        author.setName(input.name());
        author.setNationality(input.nationality());
        author.setBirthYear(input.birthYear());
        return author;
    }

    public static void updateEntity(Author author, AuthorInput input) {
        author.setName(input.name());
        author.setNationality(input.nationality());
        author.setBirthYear(input.birthYear());
    }

    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
            author.getId(),
            author.getName(),
            author.getNationality(),
            author.getBirthYear(),
            Optional.ofNullable(author.getBooks())
                .map(books -> books.stream().map(Book::getId).toList())
                .orElse(Collections.emptyList())
        );
    }
}
