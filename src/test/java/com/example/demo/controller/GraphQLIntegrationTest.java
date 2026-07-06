package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest
class GraphQLIntegrationTest {

    private final GraphQlTester graphQlTester;

    GraphQLIntegrationTest(@Autowired ExecutionGraphQlService graphQlService) {
        this.graphQlTester = ExecutionGraphQlServiceTester.builder(graphQlService).build();
    }

    @Test
    void shouldCreateAndQueryAuthor() {
        String mutation = """
            mutation {
                createAuthor(input: { name: "Victor Hugo", nationality: "Française", birthYear: 1802 }) {
                    id
                    name
                    nationality
                }
            }
        """;

        graphQlTester.document(mutation)
            .execute()
            .path("createAuthor.name")
            .entity(String.class)
            .isEqualTo("Victor Hugo");
    }

    @Test
    void shouldCreateAndQueryBook() {
        String createAuthor = """
            mutation { createAuthor(input: { name: "Author" }) { id } }
        """;
        var authorResult = graphQlTester.document(createAuthor).execute();
        Long authorId = authorResult.path("createAuthor.id").entity(Long.class).get();

        String createBook = String.format("""
            mutation {
                createBook(input: { title: "Test Book", isbn: "978-0-00-000000-3", category: "Fiction", authorId: %d }) {
                    id
                    title
                }
            }
        """, authorId);

        graphQlTester.document(createBook)
            .execute()
            .path("createBook.title")
            .entity(String.class)
            .isEqualTo("Test Book");
    }

    @Test
    void shouldReturnErrorForNonExistentAuthor() {
        graphQlTester.document("""
            query { authorById(id: 999) { name } }
        """)
        .execute()
        .errors()
        .expect(e -> e.getMessage().contains("Author not found"));
    }
}
