package com.example.demo.config;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Member;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public DataInitializer(AuthorRepository authorRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        if (authorRepository.count() > 0) return;

        Author hugo = new Author();
        hugo.setName("Victor Hugo");
        hugo.setNationality("Française");
        hugo.setBirthYear(1802);
        authorRepository.save(hugo);

        Author orwell = new Author();
        orwell.setName("George Orwell");
        orwell.setNationality("Britannique");
        orwell.setBirthYear(1903);
        authorRepository.save(orwell);

        Author rowling = new Author();
        rowling.setName("J.K. Rowling");
        rowling.setNationality("Britannique");
        rowling.setBirthYear(1965);
        authorRepository.save(rowling);

        Book miseres = new Book();
        miseres.setTitle("Les Misérables");
        miseres.setIsbn("978-2-07-040922-8");
        miseres.setCategory("Fiction");
        miseres.setAuthor(hugo);
        miseres.setAvailable(true);
        bookRepository.save(miseres);

        Book ndp = new Book();
        ndp.setTitle("Notre-Dame de Paris");
        ndp.setIsbn("978-2-07-041769-8");
        ndp.setCategory("Fiction");
        ndp.setAuthor(hugo);
        ndp.setAvailable(true);
        bookRepository.save(ndp);

        Book nineteen = new Book();
        nineteen.setTitle("1984");
        nineteen.setIsbn("978-0-14-103614-4");
        nineteen.setCategory("Dystopie");
        nineteen.setAuthor(orwell);
        nineteen.setAvailable(true);
        bookRepository.save(nineteen);

        Book farm = new Book();
        farm.setTitle("Animal Farm");
        farm.setIsbn("978-0-14-103613-7");
        farm.setCategory("Satire");
        farm.setAuthor(orwell);
        farm.setAvailable(true);
        bookRepository.save(farm);

        Book harry = new Book();
        harry.setTitle("Harry Potter à l'école des sorciers");
        harry.setIsbn("978-2-07-064302-8");
        harry.setCategory("Fantasy");
        harry.setAuthor(rowling);
        harry.setAvailable(true);
        bookRepository.save(harry);

        Member alice = new Member();
        alice.setName("Alice Martin");
        alice.setEmail("alice.martin@email.com");
        alice.setPhone("0612345678");
        memberRepository.save(alice);

        Member bob = new Member();
        bob.setName("Bob Bernard");
        bob.setEmail("bob.bernard@email.com");
        memberRepository.save(bob);
    }
}
