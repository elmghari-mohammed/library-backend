# AGENTS.md — Bibliothèque GraphQL

## Stack

- **Java 21** + **Spring Boot 4.1.0** (Spring Framework 7.x) + **Maven**
- main class: `com.example.demo.DemoApplication`
- port / config: `src/main/resources/application.yaml`
- Group: `com.example`, artifact: `demo`

## Dev commands

```bash
./mvnw spring-boot:run          # start dev server
./mvnw test                     # run all tests
./mvnw test -Dtest=DemoApplicationTests  # single test
./mvnw clean compile            # build without tests
```

## Project rules

- **Clean code & Single Responsibility** — one class = one concern. Keep methods short and focused.
- **Don't overcomplicate** — prefer the simplest working solution. No premature abstraction.
- **Respect existing elements** — follow established patterns, naming, and package structure. Never rewrite what works.
- **Use the right skill for every task** — always load the applicable skill (`skill` tool) before starting work. In particular, always invoke the `using-superpowers` skill at session start.
- **Mini-tasks with validation gates** — split every task into the smallest independent steps. Validate each step (compile/test) before moving to the next.
- No generated code, no migrations yet, no special CI pipeline.

## Architecture notes

- Package root: `com.example.demo`
- Currently only contains `DemoApplication.java` (entrypoint) and `DemoApplicationTests.java` (smoke test).
- Dependencies: Spring WebMVC, Micrometer + Graphite registry.
- This is a blank starter project — anything new you add defines the architecture going forward.
