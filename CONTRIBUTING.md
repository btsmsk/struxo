# Contributing to Struxo

Thank you for your interest in contributing to Struxo!

## Getting Started

1. Fork the repository
2. Create a feature branch from `main`: `git checkout -b feature/your-feature`
3. Make your changes following the guidelines below
4. Run both platform builds and tests before submitting
5. Open a pull request against `main`

## Development Setup

- JDK 17+
- Android Studio with KMP plugin
- Xcode 16+ (for iOS builds)

## Code Style

- Follow Kotlin coding conventions
- Use `ktlint` formatting (default IntelliJ/Android Studio formatter)
- All public APIs must have KDoc with `@param`, `@return`, `@throws` where applicable

## Architecture Rules

- **Domain layer is pure Kotlin** — no `android.*`, `androidx.*`, or `platform.*` imports
- **ViewModels call UseCases**, never repositories directly
- **DTOs stay in the data layer** — map to domain entities via `toDomain()` / `toEntity()`
- **Use `Resource<T>`** for all data operations (Loading / Success / Error)
- **Koin `module {}` blocks** for DI — no annotation-based injection
- **Fakes over mocks** in tests — MockK is incompatible with Kotlin/Native
- Every `expect` declaration needs `actual` in **both** `androidMain` and `iosMain`

See the [14 ADRs](docs/architecture/) for detailed rationale.

## Implementation Order

When adding a new feature, follow this order:

1. Domain Entity
2. Repository Interface
3. UseCase
4. DTO (`@Serializable`)
5. ApiClient helpers
6. RepositoryImpl
7. ViewModel (extends `BaseViewModel`)
8. Screen (stateful + stateless split)
9. Koin DI module
10. Navigation route
11. `expect/actual` platform implementations

## Commit Conventions

- Use clear, descriptive commit messages
- Lead with a verb: `Add`, `Fix`, `Update`, `Remove`, `Refactor`
- Keep commits focused — one logical change per commit

Examples:
```
Add login screen with email/password validation
Fix token refresh race condition in ApiClient
Update Ktor to 3.4.1 for KMP compatibility
```

## Pull Request Process

1. Ensure both builds pass:
   ```bash
   ./gradlew :androidApp:assembleDebug
   ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
   ```

2. Ensure all tests pass:
   ```bash
   ./gradlew :composeApp:allTests
   ```

3. Describe what changed and why in the PR description
4. Link related issues if applicable

## Reporting Issues

- Use GitHub Issues
- Include: steps to reproduce, expected vs actual behavior, platform (Android/iOS/both)
- For build errors, include the full Gradle output

## License

By contributing, you agree that your contributions will be licensed under the [MIT License](LICENSE).
