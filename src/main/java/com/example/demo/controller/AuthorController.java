package com.example.demo.controller;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.service.AuthorService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @QueryMapping
    public List<AuthorResponse> allAuthors() {
        return authorService.findAll();
    }

    @QueryMapping
    public AuthorResponse authorById(@Argument Long id) {
        return authorService.findById(id);
    }

    @MutationMapping
    public AuthorResponse createAuthor(@Argument AuthorInput input) {
        return authorService.create(input);
    }

    @MutationMapping
    public AuthorResponse updateAuthor(@Argument Long id, @Argument AuthorInput input) {
        return authorService.update(id, input);
    }

    @MutationMapping
    public boolean deleteAuthor(@Argument Long id) {
        return authorService.delete(id);
    }
}
