package com.example.demo.mapper;

import com.example.demo.dto.BookInput;
import com.example.demo.dto.BookResponse;
import com.example.demo.model.Author;
import com.example.demo.model.Book;

public class BookMapper {

    public static Book toEntity(BookInput input) {
        Book book = new Book();
        book.setTitle(input.title());
        book.setIsbn(input.isbn());
        book.setCategory(input.category());
        Author author = new Author();
        author.setId(input.authorId());
        book.setAuthor(author);
        return book;
    }

    public static void updateEntity(Book book, BookInput input) {
        book.setTitle(input.title());
        book.setIsbn(input.isbn());
        book.setCategory(input.category());
        Author author = new Author();
        author.setId(input.authorId());
        book.setAuthor(author);
    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getIsbn(),
            book.getCategory(),
            book.getAuthor().getId(),
            book.isAvailable()
        );
    }
}
