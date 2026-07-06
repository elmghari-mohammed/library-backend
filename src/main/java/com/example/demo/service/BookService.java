package com.example.demo.service;

import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookResponse> findAll(String category) {
        List<Book> books = (category != null && !category.isBlank())
            ? bookRepository.findByCategory(category)
            : bookRepository.findAll();
        return books.stream().map(BookMapper::toResponse).toList();
    }

    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
            .map(BookMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public List<BookResponse> findByAuthorId(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
            .map(BookMapper::toResponse)
            .toList();
    }

    @Transactional
    public BookResponse create(BookInput input) {
        if (!authorRepository.existsById(input.authorId())) {
            throw new RuntimeException("Author not found: " + input.authorId());
        }
        Book book = BookMapper.toEntity(input);
        return BookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse update(Long id, BookInput input) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        if (!authorRepository.existsById(input.authorId())) {
            throw new RuntimeException("Author not found: " + input.authorId());
        }
        BookMapper.updateEntity(book, input);
        return BookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public boolean delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
        return true;
    }
}
