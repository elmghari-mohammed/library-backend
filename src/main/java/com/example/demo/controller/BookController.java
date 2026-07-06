package com.example.demo.controller;

import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<BookResponse> allBooks(@Argument String category) {
        return bookService.findAll(category);
    }

    @QueryMapping
    public BookResponse bookById(@Argument Long id) {
        return bookService.findById(id);
    }

    @QueryMapping
    public List<BookResponse> booksByAuthor(@Argument Long authorId) {
        return bookService.findByAuthorId(authorId);
    }

    @MutationMapping
    public BookResponse createBook(@Argument @Valid BookInput input) {
        return bookService.create(input);
    }

    @MutationMapping
    public BookResponse updateBook(@Argument Long id, @Argument @Valid BookInput input) {
        return bookService.update(id, input);
    }

    @MutationMapping
    public boolean deleteBook(@Argument Long id) {
        return bookService.delete(id);
    }
}
