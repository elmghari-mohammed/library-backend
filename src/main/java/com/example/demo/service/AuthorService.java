package com.example.demo.service;

import com.example.demo.dto.AuthorInput;
import com.example.demo.dto.AuthorResponse;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorResponse> findAll() {
        return authorRepository.findAll().stream()
            .map(AuthorMapper::toResponse)
            .toList();
    }

    public AuthorResponse findById(Long id) {
        return authorRepository.findById(id)
            .map(AuthorMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Author not found: " + id));
    }

    @Transactional
    public AuthorResponse create(AuthorInput input) {
        Author author = AuthorMapper.toEntity(input);
        return AuthorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorInput input) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Author not found: " + id));
        AuthorMapper.updateEntity(author, input);
        return AuthorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    public boolean delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found: " + id);
        }
        authorRepository.deleteById(id);
        return true;
    }
}
