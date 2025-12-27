# Repository Guidelines

## Project Structure & Module Organization
- Single Gradle module `app` contains all code; entry point is `app/src/main/java/tarot/app/TarotCliApp.java`.
- Domain, deck, draw, repository, service, and spread logic live under `app/src/main/java/tarot`.
- CLI resources (card data) are stored in `app/src/main/resources/tarot/cards.json`.
- Tests belong in `app/src/test/java` with supporting fixtures in `app/src/test/resources`.

## Build, Test, and Development Commands
- `./gradlew build` — compile, run unit tests, and produce artifacts.
- `./gradlew :app:run` — launch the interactive CLI using Java 21 toolchain.
- `./gradlew :app:test` — execute the JUnit Jupiter test suite.
- `./gradlew :app:distZip` — create the distributable ZIP under `app/build/distributions/`.

## Coding Style & Naming Conventions
- Java 21, Gradle Kotlin DSL. Use 4-space indentation and UTF-8 source files.
- Package names stay under `tarot.*`; classes PascalCase, methods/fields camelCase, constants UPPER_SNAKE.
- Prefer immutable data where possible; keep constructors simple and favor small focused methods.
- When parsing or serializing cards, use the existing Gson setup instead of custom parsers.

## Testing Guidelines
- Framework: JUnit Jupiter 5 (`org.junit.jupiter`); new tests should extend the existing `*Test` suffix pattern (e.g., `SpreadResultTest`).
- Place scenario fixtures in `app/src/test/resources`; keep assertions focused on spread/card behavior and CLI outputs.
- Run `./gradlew :app:test` locally before submitting; add edge cases for draw randomness, reversed logic, and pattern selection.

## Commit & Pull Request Guidelines
- Follow Conventional Commit style seen in history (`feat(scope): message`); keep scope aligned with the package you touched (e.g., `spread`, `deck`).
- Commits should be small and narrative; reference issues when applicable.
- Pull requests: include a concise description, testing commands executed, and sample CLI output if user-facing behavior changed. Note any data updates to `cards.json`.

## Security & Configuration Tips
- Do not commit secrets or external API keys; the app operates fully offline.
- Validate JSON changes against the existing schema expectations (card names, suits, arcana) to avoid runtime parse errors.

## Answer Tips
- Answer user's questions in CHINESE
