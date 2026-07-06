package com.example.demo.service;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock private AuthorRepository authorRepository;
    @InjectMocks private AuthorService authorService;

    @Test
    void shouldReturnAllAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(new Author()));
        List<AuthorResponse> result = authorService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindAuthorById() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Victor Hugo");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        AuthorResponse result = authorService.findById(1L);
        assertThat(result.name()).isEqualTo("Victor Hugo");
    }

    @Test
    void shouldCreateAuthor() {
        AuthorInput input = new AuthorInput("Victor Hugo", "Française", 1802);
        Author saved = new Author();
        saved.setId(1L);
        saved.setName("Victor Hugo");
        when(authorRepository.save(any())).thenReturn(saved);

        AuthorResponse result = authorService.create(input);
        assertThat(result.id()).isEqualTo(1L);
    }
}
